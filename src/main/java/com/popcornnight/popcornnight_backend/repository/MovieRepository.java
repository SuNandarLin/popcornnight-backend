package com.popcornnight.popcornnight_backend.repository;

import com.popcornnight.popcornnight_backend.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
