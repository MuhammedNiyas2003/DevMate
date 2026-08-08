package com.devmate.role.service;

import com.devmate.role.dto.request.RoleRequest;
import com.devmate.role.dto.response.RoleResponse;

import java.util.List;
import java.util.UUID;

public interface RoleService {

    RoleResponse create(RoleRequest request);

    List<RoleResponse> getAll();

    RoleResponse getById(UUID id);

    void delete(UUID id);
}