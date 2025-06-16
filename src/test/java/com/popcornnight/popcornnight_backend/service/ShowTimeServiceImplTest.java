package com.popcornnight.popcornnight_backend.service;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.popcornnight.popcornnight_backend.converter.ShowtimeConverter;
import com.popcornnight.popcornnight_backend.dto.showtime.ShowTimeRequest;
import com.popcornnight.popcornnight_backend.dto.showtime.ShowTimeRequests;
import com.popcornnight.popcornnight_backend.dto.showtime.ShowTimeResponse;
import com.popcornnight.popcornnight_backend.entity.Hall;
import com.popcornnight.popcornnight_backend.entity.Movie;
import com.popcornnight.popcornnight_backend.entity.ShowTime;
import com.popcornnight.popcornnight_backend.repository.HallRepository;
import com.popcornnight.popcornnight_backend.repository.MovieRepository;
import com.popcornnight.popcornnight_backend.repository.ShowTimeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

class ShowTimeServiceImplTest {

    @Mock
    private ShowTimeRepository showTimeRepository;
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private HallRepository hallRepository;
    @Mock
    private ShowtimeConverter showtimeConverter;

    @InjectMocks
    private ShowTimeServiceImpl showTimeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createMultipleShowTimes_shouldReturnResponses() {
        // Arrange
        ShowTimeRequest req = new ShowTimeRequest();
        req.setTimeslot("10:00-12:00");
        req.setTimestamp(1750065306L);
        req.setIsPublished(true);
        req.setSeatStatusGrid(List.of(List.of(1, 0, 1)));
        req.setPrice(120.0f);
        req.setMovieId(2L);
        req.setHallId(1L);

        ShowTimeRequests requests = new ShowTimeRequests();
        requests.setShowtimes(List.of(req));

        Hall hall = Hall.builder().id(1L).build();
        Movie movie = Movie.builder().id(2L).build();
        ShowTime showTime = ShowTime.builder()
                .id(10L)
                .timeslot(req.getTimeslot())
                .timestamp(req.getTimestamp())
                .isPublished(req.getIsPublished())
                .seatStatusGrid(req.getSeatStatusGrid())
                .price(req.getPrice())
                .movie(movie)
                .hall(hall)
                .build();
        ShowTimeResponse response = ShowTimeResponse.builder().id(10L).build();

        when(hallRepository.findById(1L)).thenReturn(Optional.of(hall));
        when(movieRepository.findById(2L)).thenReturn(Optional.of(movie));
        when(showTimeRepository.saveAll(anyList())).thenReturn(List.of(showTime));
        when(showtimeConverter.convertToShowTimeResponse(showTime)).thenReturn(response);

        // Act
        List<ShowTimeResponse> result = showTimeService.createMultipleShowTimes(requests);

        // Assert
        assertEquals(1, result.size());
        assertEquals(10L, result.get(0).getId());
    }

    @Test
    void getShowTimesByDate_shouldReturnResponses() {
        // Arrange
        LocalDate date = LocalDate.of(2025, 6, 16);
        ZoneId zone = ZoneId.systemDefault();
        long start = date.atStartOfDay(zone).toInstant().toEpochMilli();
        long end = date.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli();

        ShowTime showTime = ShowTime.builder().id(10L).timestamp(start + 1000).build();
        ShowTimeResponse response = ShowTimeResponse.builder().id(10L).build();

        when(showTimeRepository.findByTimestampBetween(start, end)).thenReturn(List.of(showTime));
        when(showtimeConverter.convertToShowTimeResponse(showTime)).thenReturn(response);

        // Act
        List<ShowTimeResponse> result = showTimeService.getShowTimesByDate(date);

        // Assert
        assertEquals(1, result.size());
        assertEquals(10L, result.get(0).getId());
    }
}