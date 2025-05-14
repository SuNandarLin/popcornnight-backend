package com.popcornnight.popcornnight_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.popcornnight.popcornnight_backend.entity.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
}