package com.devmate.ticket.dto;

import com.devmate.ticket.entity.ActivityType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ActivityResponse {
    private UUID id;
    private ActivityType activityType;
    private String description;
    private String performedBy;
    private LocalDateTime createdAt;
}