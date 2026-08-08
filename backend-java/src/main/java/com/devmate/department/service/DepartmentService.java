package com.devmate.department.service;

import com.devmate.department.dto.request.DepartmentRequest;
import com.devmate.department.dto.response.DepartmentResponse;

import java.util.List;
import java.util.UUID;

public interface DepartmentService {

    DepartmentResponse create(DepartmentRequest request);

    List<DepartmentResponse> getAll();

    DepartmentResponse getById(UUID id);

    void delete(UUID id);
}