package com.devmate.role.service;

import com.devmate.enums.RoleType;
import com.devmate.role.entity.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleService {

    Role save(Role role);

    List<Role> findAll();

    Optional<Role> findById(UUID id);

    Optional<Role> findByName(RoleType roleType);

    void delete(UUID id);
}