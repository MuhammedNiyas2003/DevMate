package com.devmate.user.service;

import com.devmate.user.dto.request.UserRequest;
import com.devmate.user.dto.response.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponse create(UserRequest request);

    List<UserResponse> getAll();

    UserResponse getById(UUID id);

    void delete(UUID id);
}