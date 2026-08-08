package com.devmate.role.service.impl;

import com.devmate.role.dto.request.RoleRequest;
import com.devmate.role.dto.response.RoleResponse;
import com.devmate.role.entity.Role;
import com.devmate.role.mapper.RoleMapper;
import com.devmate.role.repository.RoleRepository;
import com.devmate.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public RoleResponse create(RoleRequest request) {
        Role role = RoleMapper.toEntity(request);
        Role saved = roleRepository.save(role);
        return RoleMapper.toResponse(saved);
    }

    @Override
    public List<RoleResponse> getAll() {
        return roleRepository.findAll()
                .stream()
                .map(RoleMapper::toResponse)
                .toList();
    }

    @Override
    public RoleResponse getById(UUID id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        return RoleMapper.toResponse(role);
    }

    @Override
    public void delete(UUID id) {
        roleRepository.deleteById(id);
    }
}