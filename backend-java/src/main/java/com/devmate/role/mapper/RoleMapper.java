package com.devmate.role.mapper;

import com.devmate.role.dto.request.RoleRequest;
import com.devmate.role.dto.response.RoleResponse;
import com.devmate.role.entity.Role;

public class RoleMapper {

    private RoleMapper() {
    }

    public static Role toEntity(RoleRequest request) {
        Role role = new Role();
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        role.setActive(request.isActive());
        return role;
    }

    public static RoleResponse toResponse(Role role) {
        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .active(role.isActive())
                .createdAt(role.getCreatedAt())
                .updatedAt(role.getUpdatedAt())
                .build();
    }
}