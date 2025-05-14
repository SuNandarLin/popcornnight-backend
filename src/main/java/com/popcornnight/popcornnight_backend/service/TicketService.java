package com.popcornnight.popcornnight_backend.service;

import com.popcornnight.popcornnight_backend.entity.Ticket;

import java.util.List;

public interface TicketService {
    List<Ticket> getAllTickets();

    Ticket getTicketById(Long ticketId);

    Ticket createTicket(Ticket ticket);

    Ticket updateTicket(Long ticketId, Ticket ticket);

    void deleteTicket(Long ticketId);
}