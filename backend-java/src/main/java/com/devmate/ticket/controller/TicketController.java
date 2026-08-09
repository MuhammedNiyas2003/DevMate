package com.devmate.ticket.controller;

import com.devmate.common.response.ApiResponse;
import com.devmate.ticket.dto.TicketRequest;
import com.devmate.ticket.dto.TicketResponse;
import com.devmate.ticket.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;

import com.devmate.ticket.dto.ActivityResponse;
import com.devmate.ticket.dto.CommentRequest;
import com.devmate.ticket.dto.CommentResponse;
import com.devmate.ticket.dto.StatusUpdateRequest;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TicketResponse> create(@Valid @RequestBody TicketRequest request,
            Authentication authentication) {
        return ApiResponse.success("Ticket created successfully",
                ticketService.create(request, authentication));
    }

    @GetMapping
    public ApiResponse<List<TicketResponse>> getAll() {
        return ApiResponse.success("Tickets fetched successfully", ticketService.getAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<TicketResponse> getById(@PathVariable UUID id) {
        return ApiResponse.success("Ticket fetched successfully", ticketService.getById(id));
    }

    @PutMapping("/{ticketId}/assign/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<TicketResponse> assignTicket(@PathVariable UUID ticketId,
            @PathVariable UUID userId) {
        return ApiResponse.success("Ticket assigned successfully",
                ticketService.assignTicket(ticketId, userId));
    }

    @PutMapping("/{ticketId}/status")
    public ApiResponse<TicketResponse> updateStatus(@PathVariable UUID ticketId,
            @Valid @RequestBody StatusUpdateRequest request,
            Authentication authentication) {
        return ApiResponse.success("Ticket status updated successfully",
                ticketService.updateStatus(ticketId, request.getStatus(), authentication));
    }

    @PostMapping("/{ticketId}/comments")
    public ApiResponse<CommentResponse> addComment(@PathVariable UUID ticketId,
            @Valid @RequestBody CommentRequest request,
            Authentication authentication) {
        return ApiResponse.success("Comment added successfully",
                ticketService.addComment(ticketId, request, authentication));
    }

    @GetMapping("/{ticketId}/comments")
    public ApiResponse<List<CommentResponse>> getComments(@PathVariable UUID ticketId) {
        return ApiResponse.success("Comments fetched successfully",
                ticketService.getComments(ticketId));
    }

    @GetMapping("/{ticketId}/activities")
    public ApiResponse<List<ActivityResponse>> getActivities(@PathVariable UUID ticketId) {
        return ApiResponse.success("Activities fetched successfully",
                ticketService.getActivities(ticketId));
    }
}