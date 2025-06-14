package com.popcornnight.popcornnight_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.popcornnight.popcornnight_backend.dto.user.USER_ROLE;
import com.popcornnight.popcornnight_backend.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);

    Optional<User> findFirstByRole(USER_ROLE role);
}
