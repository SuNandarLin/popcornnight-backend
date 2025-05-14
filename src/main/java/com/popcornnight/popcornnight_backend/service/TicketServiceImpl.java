package com.popcornnight.popcornnight_backend.service;

import com.popcornnight.popcornnight_backend.entity.Ticket;
import com.popcornnight.popcornnight_backend.repository.TicketRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    @Override
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Override
    public Ticket getTicketById(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id " + ticketId));
    }

    @Override
    public Ticket createTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket updateTicket(Long ticketId, Ticket ticket) {
        Ticket existingTicket = getTicketById(ticketId);
        existingTicket.setPrice(ticket.getPrice());
        existingTicket.setSeatNumber(ticket.getSeatNumber());
        return ticketRepository.save(existingTicket);
    }

    @Override
    public void deleteTicket(Long ticketId) {
        ticketRepository.deleteById(ticketId);
    }
}