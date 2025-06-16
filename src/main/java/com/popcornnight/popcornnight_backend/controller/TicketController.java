package com.popcornnight.popcornnight_backend.controller;

import com.popcornnight.popcornnight_backend.dto.ticket.TicketQRcodeInfo;
import com.popcornnight.popcornnight_backend.dto.ticket.TicketRequest;
import com.popcornnight.popcornnight_backend.dto.ticket.TicketResponse;
import com.popcornnight.popcornnight_backend.service.TicketService;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@AllArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/issue")
    @ResponseStatus(HttpStatus.CREATED)
    public TicketResponse issueTicket(@RequestBody TicketRequest ticketRequest) {
        return ticketService.issueTicket(ticketRequest);
    }

    @GetMapping("/user/{userId}")
    public List<TicketResponse> getTicketsByUserId(@PathVariable Long userId) {
        return ticketService.getTicketsByUserId(userId);
    }

    @GetMapping
    public List<TicketResponse> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @GetMapping("/{id}")
    public TicketResponse getTicketById(@PathVariable Long id) {
        return ticketService.getTicketById(id);
    }

    @PutMapping("/verify")
    public boolean verifyAndRedeemTicket(@RequestBody TicketQRcodeInfo ticketQRcodeInfo) {
        return ticketService.verifyAndRedeemTicket(ticketQRcodeInfo);
    }

    @GetMapping("/redeemed")
    public List<TicketResponse> getRedeemedTickets() {
        return ticketService.getRedeemedTickets();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketResponse createTicket(@RequestBody TicketRequest ticketRequest) {
        return ticketService.createTicket(ticketRequest);
    }

    @PutMapping("/{id}")
    public TicketResponse updateTicket(@PathVariable Long id, @RequestBody TicketRequest ticketRequest) {
        return ticketService.updateTicket(id, ticketRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
    }
}