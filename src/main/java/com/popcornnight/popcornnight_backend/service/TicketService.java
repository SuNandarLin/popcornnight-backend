package com.popcornnight.popcornnight_backend.service;

import com.popcornnight.popcornnight_backend.dto.ticket.TicketRequest;
import com.popcornnight.popcornnight_backend.dto.ticket.TicketResponse;

import java.util.List;

public interface TicketService {
    List<TicketResponse> issueTicket(List<TicketRequest> ticketRequest);

    List<TicketResponse> getAllTickets();

    TicketResponse getTicketById(Long ticketId);

    TicketResponse createTicket(TicketRequest ticketRequest);

    TicketResponse updateTicket(Long ticketId, TicketRequest ticketRequest);

    void deleteTicket(Long ticketId);
}