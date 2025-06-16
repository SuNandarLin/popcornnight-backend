package com.popcornnight.popcornnight_backend.service;

import com.popcornnight.popcornnight_backend.dto.ticket.TicketQRcodeInfo;
import com.popcornnight.popcornnight_backend.dto.ticket.TicketRequest;
import com.popcornnight.popcornnight_backend.dto.ticket.TicketResponse;
import com.popcornnight.popcornnight_backend.dto.user.USER_ROLE;
import com.popcornnight.popcornnight_backend.dto.user.UserResponse;
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
    public TicketResponse issueTicket(TicketRequest ticketRequest) {
        if (ticketRequest == null || ticketRequest.getSeatNumbers() == null
                || ticketRequest.getSeatNumbers().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seat number list is empty");
        }

        Long showTimeId = ticketRequest.getShowTimeId();
        Long userId = (ticketRequest.getUserRole() == USER_ROLE.GUEST)
                ? getGuestUserId()
                : ticketRequest.getUserId();

        ShowTime showTime = showTimeRepository.findById(showTimeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Showtime not found with id " + showTimeId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "User not found with id " + userId));

        List<String> seatNumbers = ticketRequest.getSeatNumbers();

        Ticket ticket = Ticket.builder()
                .seatNumber(seatNumbers)
                .status(TICKET_STATUS.VALID)
                .user(user)
                .showTime(showTime)
                .build();
        Ticket savedTicket = ticketRepository.save(ticket);

        TicketQRcodeInfo ticketQRcodeInfo = TicketQRcodeInfo.builder()
                .qrId(java.util.UUID.randomUUID().toString())
                .ticketId(savedTicket.getId())
                .ticketStatus(TICKET_STATUS.VALID)
                .seatNumbers(seatNumbers)
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

        savedTicket.setQrId(ticketQRcodeInfo.getQrId());
        savedTicket.setQrcodeUrl(qrCodeImageUrl);
        savedTicket = ticketRepository.save(savedTicket);

        Object seatStatusGridObj = showTime.getSeatStatusGrid();
        Object seatNoGridObj = showTime.getHall().getSeatNoGrid();

        @SuppressWarnings("unchecked")
        List<List<Integer>> seatStatusGrid = (List<List<Integer>>) seatStatusGridObj;
        @SuppressWarnings("unchecked")
        List<List<String>> seatNoGrid = (List<List<String>>) seatNoGridObj;

        for (String seatNumber : seatNumbers) {
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

        return convertToTicketResponse(savedTicket);
    }

    @Override
    public List<TicketResponse> getTicketsByUserId(Long userId) {
        List<Ticket> tickets = ticketRepository.findByUserId(userId);
        return tickets.stream()
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
    public boolean verifyAndRedeemTicket(TicketQRcodeInfo ticketQRcodeInfo) {
        if (ticketQRcodeInfo == null || ticketQRcodeInfo.getQrId() == null || ticketQRcodeInfo.getTicketId() == 0L) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid QR code info");
        }

        Ticket ticket = ticketRepository.findById(ticketQRcodeInfo.getTicketId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));

        if (!ticketQRcodeInfo.getQrId().equals(ticket.getQrId())) {
            return false;
        }

        if (ticket.getStatus() != TICKET_STATUS.VALID) {
            return false;
        }

        ticket.setStatus(TICKET_STATUS.REDEEMED);
        ticketRepository.save(ticket);
        return true;
    }

    @Override
    public List<TicketResponse> getRedeemedTickets() {
        List<Ticket> redeemedTickets = ticketRepository.findByStatus(TICKET_STATUS.REDEEMED);
        return redeemedTickets.stream()
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
                .seatNumber(ticketRequest.getSeatNumbers())
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

        if (ticketRequest.getSeatNumbers() != null && !ticketRequest.getSeatNumbers().isEmpty()) {
            existingTicket.setSeatNumber(ticketRequest.getSeatNumbers());
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
        UserResponse userResponse = UserResponse.builder()
                .name(ticket.getUser().getName()).build();

        return TicketResponse.builder()
                .id(ticket.getId())
                .price(ticket.getShowTime().getPrice())
                .seatNumbers(ticket.getSeatNumber())
                .status(ticket.getStatus())
                .qrcodeUrl(ticket.getQrcodeUrl())
                .user(userResponse)
                .userId(ticket.getUser().getId())
                .showTimeId(ticket.getShowTime().getId())
                .showTime(showtimeConverter.convertToShowTimeResponse(ticket.getShowTime()))
                .build();
    }
}