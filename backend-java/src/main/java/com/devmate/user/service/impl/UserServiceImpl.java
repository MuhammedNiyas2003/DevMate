package com.devmate.user.service.impl;

import com.devmate.department.entity.Department;
import com.devmate.department.repository.DepartmentRepository;
import com.devmate.role.entity.Role;
import com.devmate.role.repository.RoleRepository;
import com.devmate.user.dto.request.UserRequest;
import com.devmate.user.dto.response.UserResponse;
import com.devmate.user.entity.User;
import com.devmate.user.mapper.UserMapper;
import com.devmate.user.repository.UserRepository;
import com.devmate.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.devmate.common.exception.DuplicateResourceException;
import com.devmate.common.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse create(UserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        Department department = null;
        if (request.getDepartmentId() != null) {
            department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // temporary plain text
        user.setPhoneNumber(request.getPhoneNumber());
        user.setActive(request.isActive());
        user.setRole(role);
        user.setDepartment(department);

        User saved = userRepository.save(user);
        return UserMapper.toResponse(saved);
    }

    @Override
    public List<UserResponse> getAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    @Override
    public UserResponse getById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return UserMapper.toResponse(user);
    }

    @Override
    public void delete(UUID id) {
        userRepository.deleteById(id);
    }
}