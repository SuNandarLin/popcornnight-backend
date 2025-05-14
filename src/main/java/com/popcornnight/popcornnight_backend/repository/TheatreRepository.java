package com.popcornnight.popcornnight_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.popcornnight.popcornnight_backend.entity.Theatre;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre, Long> {
}