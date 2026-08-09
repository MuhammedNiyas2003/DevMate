package com.devmate.ticket.dto;

import com.devmate.ticket.entity.TicketPriority;
import com.devmate.ticket.entity.TicketStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TicketResponse {
    private UUID id;
    private String title;
    private String description;
    private TicketStatus status;
    private TicketPriority priority;
    private String createdByEmail;
    private String assignedToEmail;
    private String departmentName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}