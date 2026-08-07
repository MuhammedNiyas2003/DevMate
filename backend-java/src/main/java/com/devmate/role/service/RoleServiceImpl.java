package com.devmate.role.service;

import com.devmate.enums.RoleType;
import com.devmate.role.entity.Role;
import com.devmate.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> findById(UUID id) {
        return roleRepository.findById(id);
    }

    @Override
    public Optional<Role> findByName(RoleType roleType) {
        return roleRepository.findByName(roleType);
    }

    @Override
    public void delete(UUID id) {
        roleRepository.deleteById(id);
    }
}