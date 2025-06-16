package com.popcornnight.popcornnight_backend.service;

import com.popcornnight.popcornnight_backend.dto.ticket.TicketQRcodeInfo;
import com.popcornnight.popcornnight_backend.dto.ticket.TicketRequest;
import com.popcornnight.popcornnight_backend.dto.ticket.TicketResponse;

import java.util.List;

public interface TicketService {
    TicketResponse issueTicket(TicketRequest ticketRequest);

    List<TicketResponse> getTicketsByUserId(Long userId);

    List<TicketResponse> getAllTickets();

    boolean verifyAndRedeemTicket(TicketQRcodeInfo ticketQRcodeInfo);

    List<TicketResponse> getRedeemedTickets();

    TicketResponse getTicketById(Long ticketId);

    TicketResponse createTicket(TicketRequest ticketRequest);

    TicketResponse updateTicket(Long ticketId, TicketRequest ticketRequest);

    void deleteTicket(Long ticketId);
}