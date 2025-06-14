package com.popcornnight.popcornnight_backend.service;

import com.popcornnight.popcornnight_backend.dto.ticket.TicketQRcodeInfo;
import com.popcornnight.popcornnight_backend.dto.ticket.TicketRequest;
import com.popcornnight.popcornnight_backend.dto.ticket.TicketResponse;
import com.popcornnight.popcornnight_backend.dto.user.USER_ROLE;
import com.popcornnight.popcornnight_backend.converter.ShowtimeConverter;
import com.popcornnight.popcornnight_backend.dto.ticket.TICKET_STATUS;
import com.popcornnight.popcornnight_backend.entity.ShowTime;
import com.popcornnight.popcornnight_backend.entity.Ticket;
import com.popcornnight.popcornnight_backend.entity.User;
import com.popcornnight.popcornnight_backend.repository.ShowTimeRepository;
import com.popcornnight.popcornnight_backend.repository.TicketRepository;
import com.popcornnight.popcornnight_backend.repository.UserRepository;
import com.popcornnight.popcornnight_backend.utils.QRCodeGenerator;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final ShowTimeRepository showTimeRepository;
    private final FirebaseStorageService firebaseStorageService;
    private final ShowtimeConverter showtimeConverter;

    // public TicketServiceImpl(
    // TicketRepository ticketRepository,
    // UserRepository userRepository,
    // ShowTimeRepository showTimeRepository,
    // FirebaseStorageService firebaseStorageService) {
    // this.ticketRepository = ticketRepository;
    // this.userRepository = userRepository;
    // this.showTimeRepository = showTimeRepository;
    // this.firebaseStorageService = firebaseStorageService;
    // }

    private Long guestUserId;

    @PostConstruct
    public void initGuestUserId() {
        this.guestUserId = userRepository.findFirstByRole(USER_ROLE.GUEST)
                .map(User::getId)
                .orElseThrow(() -> new IllegalStateException("No guest user found on startup"));
    }

    public Long getGuestUserId() {
        return guestUserId;
    }

    @Override
    public List<TicketResponse> issueTicket(List<TicketRequest> ticketRequests) {
        if (ticketRequests == null || ticketRequests.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ticket request list is empty");
        }

        Long userId = (ticketRequests.get(0).getUserRole() == USER_ROLE.GUEST)
                ? getGuestUserId()
                : ticketRequests.get(0).getUserId();
        Long showTimeId = ticketRequests.get(0).getShowTimeId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "User not found with id " + userId));

        ShowTime showTime = showTimeRepository.findById(showTimeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Showtime not found with id " + showTimeId));

        List<Ticket> ticketsToSave = ticketRequests.stream().map(req -> Ticket.builder()
                .seatNumber(req.getSeatNumber())
                .price(req.getPrice())
                .status(TICKET_STATUS.VALID)
                .user(user)
                .showTime(showTime)
                .build()).collect(Collectors.toList());

        List<Ticket> savedTickets = ticketRepository.saveAll(ticketsToSave);

        for (int i = 0; i < savedTickets.size(); i++) {
            Ticket ticket = savedTickets.get(i);

            TicketQRcodeInfo ticketQRcodeInfo = TicketQRcodeInfo.builder()
                    .qrId(java.util.UUID.randomUUID().toString())
                    .ticketId(ticket.getId())
                    .ticketStatus(TICKET_STATUS.VALID)
                    .seatNumber(ticket.getSeatNumber())
                    .movieTitle(showTime.getMovie().getTitle())
                    .showTimeslot(showTime.getTimeslot())
                    .geneartedAt(System.currentTimeMillis())
                    .build();

            String qrCodeImageUrl = "";
            try {
                BufferedImage qrImage = QRCodeGenerator.generateQRCodeImage(ticketQRcodeInfo, 300, 300);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(qrImage, "PNG", baos);
                byte[] qrImageBytes = baos.toByteArray();
                String uniqueFileName = ticketQRcodeInfo.getQrId() + ".png";
                qrCodeImageUrl = firebaseStorageService.uploadQRCode(qrImageBytes, uniqueFileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ticket.setQrcodeUrl(qrCodeImageUrl);
        }

        List<Ticket> updatedTickets = ticketRepository.saveAll(savedTickets);

        // Update seatStatusGrid after everything is successfully finished
        Object seatStatusGridObj = showTime.getSeatStatusGrid();
        Object seatNoGridObj = showTime.getHall().getSeatNoGrid();

        @SuppressWarnings("unchecked")
        List<List<Integer>> seatStatusGrid = (List<List<Integer>>) seatStatusGridObj;
        @SuppressWarnings("unchecked")
        List<List<String>> seatNoGrid = (List<List<String>>) seatNoGridObj;

        for (TicketRequest req : ticketRequests) {
            String seatNumber = req.getSeatNumber();
            boolean found = false;
            for (int i = 0; i < seatNoGrid.size(); i++) {
                List<String> row = seatNoGrid.get(i);
                for (int j = 0; j < row.size(); j++) {
                    if (seatNumber.equals(row.get(j))) {
                        seatStatusGrid.get(i).set(j, 1);
                        found = true;
                        break;
                    }
                }
                if (found)
                    break;
            }
        }
        showTime.setSeatStatusGrid(seatStatusGrid);
        showTimeRepository.save(showTime);

        return updatedTickets.stream()
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
        if (ticketRequest.getStatus() != null) {
            existingTicket.setStatus(ticketRequest.getStatus());
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
                .userId(ticket.getUser().getId())
                .showTimeId(ticket.getShowTime().getId())
                .showTime(showtimeConverter.convertToShowTimeResponse(ticket.getShowTime()))
                .build();
    }
}