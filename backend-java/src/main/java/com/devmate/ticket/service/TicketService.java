package com.devmate.ticket.service;

import com.devmate.common.exception.ResourceNotFoundException;
import com.devmate.department.entity.Department;
import com.devmate.department.repository.DepartmentRepository;
import com.devmate.ticket.dto.TicketRequest;
import com.devmate.ticket.dto.TicketResponse;
import com.devmate.ticket.entity.ActivityType;
import com.devmate.ticket.entity.Ticket;
import com.devmate.ticket.entity.TicketActivity;
import com.devmate.ticket.entity.TicketStatus;
import com.devmate.ticket.repository.TicketActivityRepository;
import com.devmate.ticket.repository.TicketCommentRepository;
import com.devmate.ticket.repository.TicketRepository;
import com.devmate.user.entity.User;
import com.devmate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

import com.devmate.ticket.dto.ActivityResponse;
import com.devmate.ticket.dto.CommentRequest;
import com.devmate.ticket.dto.CommentResponse;
import com.devmate.ticket.entity.TicketComment;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketService {

        private final TicketRepository ticketRepository;
        private final UserRepository userRepository;
        private final DepartmentRepository departmentRepository;
        private final TicketCommentRepository ticketCommentRepository;
        private final TicketActivityRepository ticketActivityRepository;

        public TicketResponse create(TicketRequest request, Authentication authentication) {

                User currentUser = userRepository.findByEmail(authentication.getName())
                                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

                Department department = departmentRepository.findById(request.getDepartmentId())
                                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

                Ticket ticket = new Ticket();
                ticket.setTitle(request.getTitle());
                ticket.setDescription(request.getDescription());
                ticket.setPriority(request.getPriority());
                ticket.setDepartment(department);
                ticket.setCreatedBy(currentUser);

                Ticket saved = ticketRepository.saveAndFlush(ticket);

                return TicketResponse.builder()
                                .id(saved.getId())
                                .title(saved.getTitle())
                                .description(saved.getDescription())
                                .status(saved.getStatus())
                                .priority(saved.getPriority())
                                .createdByEmail(saved.getCreatedBy().getEmail())
                                .departmentName(saved.getDepartment().getName())
                                .createdAt(saved.getCreatedAt())
                                .updatedAt(saved.getUpdatedAt())
                                .build();
        }

        @Transactional(readOnly = true)
        public List<TicketResponse> getAll() {
                return ticketRepository.findAll().stream()
                                .map(ticket -> TicketResponse.builder()
                                                .id(ticket.getId())
                                                .title(ticket.getTitle())
                                                .description(ticket.getDescription())
                                                .status(ticket.getStatus())
                                                .priority(ticket.getPriority())
                                                .createdByEmail(ticket.getCreatedBy().getEmail())
                                                .assignedToEmail(ticket.getAssignedTo() != null
                                                                ? ticket.getAssignedTo().getEmail()
                                                                : null)
                                                .departmentName(ticket.getDepartment().getName())
                                                .createdAt(ticket.getCreatedAt())
                                                .updatedAt(ticket.getUpdatedAt())
                                                .build())
                                .collect(Collectors.toList());
        }

        @Transactional(readOnly = true)
        public TicketResponse getById(UUID id) {

                Ticket ticket = ticketRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

                return TicketResponse.builder()
                                .id(ticket.getId())
                                .title(ticket.getTitle())
                                .description(ticket.getDescription())
                                .status(ticket.getStatus())
                                .priority(ticket.getPriority())
                                .createdByEmail(ticket.getCreatedBy().getEmail())
                                .assignedToEmail(ticket.getAssignedTo() != null
                                                ? ticket.getAssignedTo().getEmail()
                                                : null)
                                .departmentName(ticket.getDepartment().getName())
                                .createdAt(ticket.getCreatedAt())
                                .updatedAt(ticket.getUpdatedAt())
                                .build();
        }

        public TicketResponse assignTicket(UUID ticketId, UUID userId) {

                Ticket ticket = ticketRepository.findById(ticketId)
                                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

                User assignee = userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                ticket.setAssignedTo(assignee);

                Ticket saved = ticketRepository.save(ticket);
                logActivity(saved, assignee, ActivityType.ASSIGNED, "Ticket assigned to " + assignee.getEmail());

                return TicketResponse.builder()
                                .id(saved.getId())
                                .title(saved.getTitle())
                                .description(saved.getDescription())
                                .status(saved.getStatus())
                                .priority(saved.getPriority())
                                .createdByEmail(saved.getCreatedBy().getEmail())
                                .assignedToEmail(saved.getAssignedTo().getEmail())
                                .departmentName(saved.getDepartment().getName())
                                .createdAt(saved.getCreatedAt())
                                .updatedAt(saved.getUpdatedAt())
                                .build();
        }

        private TicketResponse mapToResponse(Ticket ticket) {
                return TicketResponse.builder()
                                .id(ticket.getId())
                                .title(ticket.getTitle())
                                .description(ticket.getDescription())
                                .status(ticket.getStatus())
                                .priority(ticket.getPriority())
                                .createdByEmail(ticket.getCreatedBy().getEmail())
                                .assignedToEmail(ticket.getAssignedTo() != null
                                                ? ticket.getAssignedTo().getEmail()
                                                : null)
                                .departmentName(ticket.getDepartment().getName())
                                .createdAt(ticket.getCreatedAt())
                                .updatedAt(ticket.getUpdatedAt())
                                .build();
        }

        public TicketResponse updateStatus(UUID ticketId,
                        TicketStatus newStatus,
                        Authentication authentication) {

                Ticket ticket = ticketRepository.findById(ticketId)
                                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

                User currentUser = userRepository.findByEmail(authentication.getName())
                                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

                boolean isAdmin = currentUser.getRole().getName().name().equals("ADMIN");
                boolean isAssignee = ticket.getAssignedTo() != null &&
                                ticket.getAssignedTo().getId().equals(currentUser.getId());

                if (!isAdmin && !isAssignee) {
                        throw new org.springframework.security.access.AccessDeniedException(
                                        "You are not allowed to update this ticket");
                }

                TicketStatus current = ticket.getStatus();

                boolean valid = switch (current) {
                        case OPEN -> newStatus == TicketStatus.IN_PROGRESS;
                        case IN_PROGRESS -> newStatus == TicketStatus.RESOLVED;
                        case RESOLVED -> newStatus == TicketStatus.CLOSED;
                        case CLOSED -> false;
                };

                if (!valid) {
                        throw new IllegalArgumentException(
                                        "Invalid status transition from " + current + " to " + newStatus);
                }

                ticket.setStatus(newStatus);
                Ticket saved = ticketRepository.save(ticket);
                logActivity(saved, currentUser, ActivityType.STATUS_CHANGED,
                                "Status changed from " + current + " to " + newStatus);

                return mapToResponse(saved);
        }

        public CommentResponse addComment(UUID ticketId,
                        CommentRequest request,
                        Authentication authentication) {

                Ticket ticket = ticketRepository.findById(ticketId)
                                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

                User user = userRepository.findByEmail(authentication.getName())
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                TicketComment comment = new TicketComment();
                comment.setTicket(ticket);
                comment.setUser(user);
                comment.setComment(request.getComment());

                TicketComment saved = ticketCommentRepository.saveAndFlush(comment);
                logActivity(ticket, user, ActivityType.COMMENT_ADDED,
                                "Comment added");

                return CommentResponse.builder()
                                .id(saved.getId())
                                .comment(saved.getComment())
                                .authorEmail(saved.getUser().getEmail())
                                .createdAt(saved.getCreatedAt())
                                .build();
        }

        @Transactional(readOnly = true)
        public List<CommentResponse> getComments(UUID ticketId) {

                ticketRepository.findById(ticketId)
                                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

                return ticketCommentRepository.findByTicketIdOrderByCreatedAtAsc(ticketId)
                                .stream()
                                .map(comment -> CommentResponse.builder()
                                                .id(comment.getId())
                                                .comment(comment.getComment())
                                                .authorEmail(comment.getUser().getEmail())
                                                .createdAt(comment.getCreatedAt())
                                                .build())
                                .collect(Collectors.toList());
        }

        private void logActivity(Ticket ticket, User user,
                        ActivityType type, String description) {
                TicketActivity activity = new TicketActivity();
                activity.setTicket(ticket);
                activity.setUser(user);
                activity.setActivityType(type);
                activity.setDescription(description);
                ticketActivityRepository.save(activity);
        }

        @Transactional(readOnly = true)
        public List<ActivityResponse> getActivities(UUID ticketId) {
                return ticketActivityRepository.findByTicketIdOrderByCreatedAtAsc(ticketId)
                                .stream()
                                .map(a -> ActivityResponse.builder()
                                                .id(a.getId())
                                                .activityType(a.getActivityType())
                                                .description(a.getDescription())
                                                .performedBy(a.getUser().getEmail())
                                                .createdAt(a.getCreatedAt())
                                                .build())
                                .toList();
        }
}