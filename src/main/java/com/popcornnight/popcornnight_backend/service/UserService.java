package com.popcornnight.popcornnight_backend.service;

import com.popcornnight.popcornnight_backend.dto.user.UserResponse;

import java.util.List;

import com.popcornnight.popcornnight_backend.dto.user.UserRequest;

public interface UserService {

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long userId);

    UserResponse createUser(UserRequest userRequest);

    UserResponse updateUser(Long userId, UserRequest userRequest);

    String deleteUser(Long userId);

}
