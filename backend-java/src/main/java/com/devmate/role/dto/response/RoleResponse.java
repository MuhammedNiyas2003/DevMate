package com.devmate.role.dto.response;

import com.devmate.enums.RoleType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class RoleResponse {

    private UUID id;
    private RoleType name;
    private String description;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}