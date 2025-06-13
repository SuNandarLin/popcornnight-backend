package com.popcornnight.popcornnight_backend.service;

import com.popcornnight.popcornnight_backend.dto.ticket.TicketRequest;
import com.popcornnight.popcornnight_backend.dto.ticket.TicketResponse;
import com.popcornnight.popcornnight_backend.entity.ShowTime;
import com.popcornnight.popcornnight_backend.entity.Ticket;
import com.popcornnight.popcornnight_backend.entity.User;
import com.popcornnight.popcornnight_backend.repository.ShowTimeRepository;
import com.popcornnight.popcornnight_backend.repository.TicketRepository;
import com.popcornnight.popcornnight_backend.repository.UserRepository;
import com.popcornnight.popcornnight_backend.utils.QRCodeGenerator;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

@Service
@AllArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final ShowTimeRepository showTimeRepository;
    private final FirebaseStorageService firebaseStorageService;

    @Override
    public List<TicketResponse> issueTicket(List<TicketRequest> ticketRequests) {
        if (ticketRequests == null || ticketRequests.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ticket request list is empty");
        }

        Long userId = ticketRequests.get(0).getUserId();
        Long showTimeId = ticketRequests.get(0).getShowTimeId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "User not found with id " + userId));

        ShowTime showTime = showTimeRepository.findById(showTimeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Showtime not found with id " + showTimeId));

        List<Ticket> tickets = ticketRequests.stream().map(req -> {
            String qrContent = String.format(
                    "showTimeId:%d|movie:%s|seat:%s|timestamp:%d|uuid:%s",
                    showTime.getId(),
                    showTime.getMovie() != null ? showTime.getMovie().getTitle() : "",
                    req.getSeatNumber(),
                    System.currentTimeMillis(),
                    java.util.UUID.randomUUID().toString());
            byte[] qrImageBytes = new byte[0];
            try {
                BufferedImage qrImage = QRCodeGenerator.generateQRCodeImage(qrContent, 300, 300);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(qrImage, "PNG", baos);
                qrImageBytes = baos.toByteArray();

            } catch (Exception e) {
                e.printStackTrace();
            }
            // Upload to Firebase
            String uniqueFileName = UUID.randomUUID() + ".png";
            String publicUrl = firebaseStorageService.uploadQRCode(qrImageBytes, uniqueFileName);

            return Ticket.builder()
                    .seatNumber(req.getSeatNumber())
                    .price((float) 123.0)
                    .status("VALID")
                    .qrcodeUrl(publicUrl)
                    .user(user)
                    .showTime(showTime)
                    .build();
        }).collect(Collectors.toList());

        List<Ticket> savedTickets = ticketRepository.saveAll(tickets);

        return savedTickets.stream()
                .map(this::convertToTicketResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketResponse> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(this::convertToTicketResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TicketResponse getTicketById(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id " + ticketId));
        return convertToTicketResponse(ticket);
    }

    @Override
    public TicketResponse createTicket(TicketRequest ticketRequest) {
        User user = userRepository.findById(ticketRequest.getUserId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "User not found with id " + ticketRequest.getUserId()));

        ShowTime showTime = showTimeRepository.findById(ticketRequest.getShowTimeId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Showtime not found with id " + ticketRequest.getShowTimeId()));

        Ticket ticket = Ticket.builder()
                .seatNumber(ticketRequest.getSeatNumber())
                .price(ticketRequest.getPrice())
                .status(ticketRequest.getStatus())
                .qrcodeUrl(ticketRequest.getQrcodeUrl())
                .user(user)
                .showTime(showTime)
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);
        return convertToTicketResponse(savedTicket);
    }

    @Override
    public TicketResponse updateTicket(Long ticketId, TicketRequest ticketRequest) {
        Ticket existingTicket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Ticket not found with id " + ticketId));

        if (ticketRequest.getPrice() != null && !ticketRequest.getPrice().isNaN()) {
            existingTicket.setPrice(ticketRequest.getPrice());
        }
        if (ticketRequest.getSeatNumber() != null && !ticketRequest.getSeatNumber().isEmpty()) {
            existingTicket.setSeatNumber(ticketRequest.getSeatNumber());
        }
        if (ticketRequest.getStatus() != null && !ticketRequest.getStatus().isEmpty()) {
            existingTicket.setStatus(ticketRequest.getStatus());
        }
        if (ticketRequest.getQrcodeUrl() != null && !ticketRequest.getQrcodeUrl().isEmpty()) {
            existingTicket.setQrcodeUrl(ticketRequest.getQrcodeUrl());
        }
        if (ticketRequest.getUserId() != null && ticketRequest.getUserId() > 0) {
            User user = userRepository.findById(ticketRequest.getUserId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "User not found with id " + ticketRequest.getUserId()));
            existingTicket.setUser(user);
        }
        if (ticketRequest.getShowTimeId() != null && ticketRequest.getShowTimeId() > 0) {
            ShowTime showTime = showTimeRepository.findById(ticketRequest.getShowTimeId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "ShowTime not found with id " + ticketRequest.getShowTimeId()));
            existingTicket.setShowTime(showTime);
        }
        Ticket updatedTicket = ticketRepository.save(existingTicket);
        return convertToTicketResponse(updatedTicket);
    }

    @Override
    public void deleteTicket(Long ticketId) {
        ticketRepository.deleteById(ticketId);
    }

    private TicketResponse convertToTicketResponse(Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .price(ticket.getPrice())
                .seatNumber(ticket.getSeatNumber())
                .status(ticket.getStatus())
                .qrcodeUrl(ticket.getQrcodeUrl())
                .user(ticket.getUser())
                .showTime(ticket.getShowTime())
                .build();
    }
}