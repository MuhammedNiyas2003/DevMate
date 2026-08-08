package com.devmate.department.service.impl;

import com.devmate.department.dto.request.DepartmentRequest;
import com.devmate.department.dto.response.DepartmentResponse;
import com.devmate.department.entity.Department;
import com.devmate.department.mapper.DepartmentMapper;
import com.devmate.department.repository.DepartmentRepository;
import com.devmate.department.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public DepartmentResponse create(DepartmentRequest request) {
        Department department = DepartmentMapper.toEntity(request);
        Department saved = departmentRepository.save(department);
        return DepartmentMapper.toResponse(saved);
    }

    @Override
    public List<DepartmentResponse> getAll() {
        return departmentRepository.findAll()
                .stream()
                .map(DepartmentMapper::toResponse)
                .toList();
    }

    @Override
    public DepartmentResponse getById(UUID id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        return DepartmentMapper.toResponse(department);
    }

    @Override
    public void delete(UUID id) {
        departmentRepository.deleteById(id);
    }
}