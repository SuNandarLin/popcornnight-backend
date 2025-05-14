package com.popcornnight.popcornnight_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.popcornnight.popcornnight_backend.entity.ShowTime;

@Repository
public interface ShowTimeRepository extends JpaRepository<ShowTime, Long> {
}