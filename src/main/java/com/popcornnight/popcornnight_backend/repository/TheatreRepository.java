package com.popcornnight.popcornnight_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.popcornnight.popcornnight_backend.entity.Theatre;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre, Long> {
    @Query("""
                SELECT DISTINCT t FROM Theatre t
                LEFT JOIN FETCH t.halls h
                LEFT JOIN FETCH h.showTimes
            """)
    List<Theatre> findAllWithHallsAndShowtimes();

}