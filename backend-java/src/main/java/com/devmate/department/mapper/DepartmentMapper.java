package com.devmate.department.mapper;

import com.devmate.department.dto.request.DepartmentRequest;
import com.devmate.department.dto.response.DepartmentResponse;
import com.devmate.department.entity.Department;

public class DepartmentMapper {

    private DepartmentMapper() {
    }

    public static Department toEntity(DepartmentRequest request) {
        Department department = new Department();
        department.setName(request.getName());
        department.setDescription(request.getDescription());
        department.setActive(request.isActive());
        return department;
    }

    public static DepartmentResponse toResponse(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .active(department.isActive())
                .createdAt(department.getCreatedAt())
                .updatedAt(department.getUpdatedAt())
                .build();
    }
}