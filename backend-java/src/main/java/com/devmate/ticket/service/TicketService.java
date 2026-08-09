package com.devmate.ticket.service;

import com.devmate.common.exception.ResourceNotFoundException;
import com.devmate.department.entity.Department;
import com.devmate.department.repository.DepartmentRepository;
import com.devmate.ticket.dto.TicketRequest;
import com.devmate.ticket.dto.TicketResponse;
import com.devmate.ticket.entity.Ticket;
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

@Service
@RequiredArgsConstructor
@Transactional
public class TicketService {

        private final TicketRepository ticketRepository;
        private final UserRepository userRepository;
        private final DepartmentRepository departmentRepository;

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
}