package com.devmate.ticket.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class CommentResponse {
    private UUID id;
    private String comment;
    private String authorEmail;
    private LocalDateTime createdAt;
}