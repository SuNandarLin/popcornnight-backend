package com.popcornnight.popcornnight_backend.service;

import com.popcornnight.popcornnight_backend.converter.ShowtimeConverter;
import com.popcornnight.popcornnight_backend.dto.ticket.TicketRequest;
import com.popcornnight.popcornnight_backend.dto.ticket.TicketResponse;
import com.popcornnight.popcornnight_backend.dto.ticket.TICKET_STATUS;
import com.popcornnight.popcornnight_backend.dto.user.USER_ROLE;
import com.popcornnight.popcornnight_backend.entity.Hall;
import com.popcornnight.popcornnight_backend.entity.Movie;
import com.popcornnight.popcornnight_backend.entity.ShowTime;
import com.popcornnight.popcornnight_backend.entity.Ticket;
import com.popcornnight.popcornnight_backend.entity.User;
import com.popcornnight.popcornnight_backend.repository.ShowTimeRepository;
import com.popcornnight.popcornnight_backend.repository.TicketRepository;
import com.popcornnight.popcornnight_backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ShowTimeRepository showTimeRepository;
    @Mock
    private FirebaseStorageService firebaseStorageService;
    @Mock
    private ShowtimeConverter showtimeConverter;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Set up guest user id if needed
        User guestUser = User.builder().id(1L).role(USER_ROLE.GUEST).build();
        when(userRepository.findFirstByRole(USER_ROLE.GUEST)).thenReturn(Optional.of(guestUser));
        ticketService.initGuestUserId();
    }

    @Test
    void issueTicket_shouldThrowException_whenSeatNumbersEmpty() {
        TicketRequest request = new TicketRequest();
        request.setSeatNumbers(null);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            ticketService.issueTicket(request);
        });
        assertEquals("400 BAD_REQUEST \"Seat number list is empty\"", ex.getMessage());
    }

    @Test
    void issueTicket_shouldReturnTicketResponse_whenValidRequest() {
        // Arrange
        TicketRequest request = new TicketRequest();
        request.setShowTimeId(10L);
        request.setUserRole(USER_ROLE.GUEST);
        request.setSeatNumbers(Arrays.asList("A1", "A2"));

        User guestUser = User.builder().id(1L).role(USER_ROLE.GUEST).name("Guest").build();
        Movie movie = Movie.builder().id(5L).title("Test Movie").build();
        Hall hall = Hall.builder().id(20L).hallNumber("1").seatNoGrid(Arrays.asList(
                Arrays.asList("A1", "A2", "A3"))).build();

        ShowTime showTime = ShowTime.builder()
                .id(10L)
                .price(100.0f)
                .seatStatusGrid(Arrays.asList(Arrays.asList(1, 0, 1)))
                .timeslot("1:00-3:30")
                .timestamp(1750065306L)
                .isPublished(false)
                .movie(movie)
                .hall(hall)
                .build();
        Ticket ticket = Ticket.builder().id(100L).seatNumber(request.getSeatNumbers()).status(TICKET_STATUS.VALID)
                .user(guestUser).showTime(showTime).build();

        when(showTimeRepository.findById(10L)).thenReturn(Optional.of(showTime));
        when(userRepository.findById(1L)).thenReturn(Optional.of(guestUser));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        // Mock QR code and Firebase upload
        when(firebaseStorageService.uploadQRCode(any(), any())).thenReturn("http://fake-url.com/qr.png");

        TicketResponse response = ticketService.issueTicket(request);

        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals(request.getSeatNumbers(), response.getSeatNumbers());
        assertEquals(TICKET_STATUS.VALID, response.getStatus());
        assertEquals("Guest", response.getUser().getName());
    }
}