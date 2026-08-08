package com.devmate.role.dto.request;

import com.devmate.enums.RoleType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RoleRequest {

    @NotNull(message = "Role name is required")
    private RoleType name;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;

    private boolean active = true;
}