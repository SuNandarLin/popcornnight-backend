package com.popcornnight.popcornnight_backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.popcornnight.popcornnight_backend.dto.user.USER_ROLE;
import com.popcornnight.popcornnight_backend.dto.user.UserRequest;
import com.popcornnight.popcornnight_backend.dto.user.UserResponse;
import com.popcornnight.popcornnight_backend.entity.User;
import com.popcornnight.popcornnight_backend.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public List<UserResponse> getAllUsers() {
        List<UserResponse> userResponseList = userRepository
                .findAll().stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .isAnonymous(user.getIsAnonymous())
                        .name(user.getName())
                        .phoneNumber(user.getPhoneNumber())
                        .email(user.getEmail())
                        .role(user.getRole().name())
                        .build())
                .collect(Collectors.toList());
        return userResponseList;
    }

    @Override
    @Transactional
    public UserResponse getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .isAnonymous(user.getIsAnonymous())
                        .name(user.getName())
                        .phoneNumber(user.getPhoneNumber())
                        .email(user.getEmail())
                        .role(user.getRole().name())
                        .build())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id " + userId));
    }

    @Override
    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        User user = User.builder()
                .isAnonymous(userRequest.getIsAnonymous())
                .name(userRequest.getName())
                .password(userRequest.getPassword())
                .phoneNumber(userRequest.getPhoneNumber())
                .email(userRequest.getEmail())
                .role(USER_ROLE.valueOf(userRequest.getRole()))
                .build();

        User savedUser = userRepository.save(user);

        return UserResponse.builder()
                .id(savedUser.getId())
                .isAnonymous(user.getIsAnonymous())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long userId, UserRequest userRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "User not found with id " + userId));

        // if (userRequest.getIsAnonymous()) {
        // user.setIsAnonymous(userRequest.getIsAnonymous());
        // }
        if (userRequest.getName() != null && !userRequest.getName().isEmpty()) {
            user.setName(userRequest.getName());
        }
        if (userRequest.getEmail() != null && !userRequest.getEmail().isEmpty()) {
            user.setEmail(userRequest.getEmail());
        }
        if (userRequest.getPhoneNumber() != null && !userRequest.getPhoneNumber().isEmpty()) {
            user.setPhoneNumber(userRequest.getPhoneNumber());
        }

        if (userRequest.getRole() != null && !userRequest.getRole().isEmpty()) {
            user.setRole(USER_ROLE.valueOf(userRequest.getRole()));
        }

        User updatedUser = userRepository.save(user);

        return UserResponse.builder()
                .id(updatedUser.getId())
                .isAnonymous(user.getIsAnonymous())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    @Override
    @Transactional
    public String deleteUser(Long userId) {
        userRepository.deleteById(userId);
        return "Success Deleting User Id " + userId;
    }
}
