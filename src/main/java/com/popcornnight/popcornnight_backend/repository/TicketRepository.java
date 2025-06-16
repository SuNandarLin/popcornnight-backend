package com.popcornnight.popcornnight_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.popcornnight.popcornnight_backend.dto.ticket.TICKET_STATUS;
import com.popcornnight.popcornnight_backend.entity.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByUserId(Long userId);

    List<Ticket> findByStatus(TICKET_STATUS redeemed);

    List<Ticket> findByShowTime_TimestampBetween(long weekStartMillis, long weekEndMillis);
}