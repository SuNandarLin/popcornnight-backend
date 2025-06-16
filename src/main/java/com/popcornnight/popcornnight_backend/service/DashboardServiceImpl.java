package com.popcornnight.popcornnight_backend.service;

import com.popcornnight.popcornnight_backend.dto.dashboard.DashboardResponse;
import com.popcornnight.popcornnight_backend.entity.Ticket;
import com.popcornnight.popcornnight_backend.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final TicketRepository ticketRepository;

    private List<Ticket> getTicketsThisWeek() {
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.with(DayOfWeek.MONDAY);
        LocalDate weekEnd = weekStart.plusDays(7);
        long weekStartMillis = weekStart.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long weekEndMillis = weekEnd.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return ticketRepository.findByShowTime_TimestampBetween(weekStartMillis, weekEndMillis);
    }

    @Override
    public int getTotalRevenueThisWeek() {
        return getTicketsThisWeek().stream()
                .mapToInt(t -> t.getShowTime() != null && t.getShowTime().getPrice() != null
                        ? t.getShowTime().getPrice().intValue()
                                * (t.getSeatNumber() != null ? t.getSeatNumber().size() : 1)
                        : 0)
                .sum();
    }

    @Override
    public int getTotalTicketsThisWeek() {
        return getTicketsThisWeek().stream()
                .mapToInt(t -> t.getSeatNumber() != null ? t.getSeatNumber().size() : 1)
                .sum();
    }

    @Override
    public List<DashboardResponse.MovieRevenue> getRevenueByMovie() {
        Map<String, Integer> revenueByMovie = new HashMap<>();
        for (Ticket t : getTicketsThisWeek()) {
            if (t.getShowTime() != null && t.getShowTime().getMovie() != null) {
                String movieTitle = t.getShowTime().getMovie().getTitle();
                int ticketCount = t.getSeatNumber() != null ? t.getSeatNumber().size() : 1;
                int revenue = t.getShowTime().getPrice() != null ? t.getShowTime().getPrice().intValue() * ticketCount
                        : 0;
                revenueByMovie.merge(movieTitle, revenue, Integer::sum);
            }
        }
        return revenueByMovie.entrySet().stream()
                .map(e -> DashboardResponse.MovieRevenue.builder().movie(e.getKey()).revenue(e.getValue()).build())
                .collect(Collectors.toList());
    }

    @Override
    public List<DashboardResponse.MovieTicketSales> getMovieTicketSales() {
        Map<String, Integer> ticketsByMovie = new HashMap<>();
        for (Ticket t : getTicketsThisWeek()) {
            if (t.getShowTime() != null && t.getShowTime().getMovie() != null) {
                String movieTitle = t.getShowTime().getMovie().getTitle();
                int ticketCount = t.getSeatNumber() != null ? t.getSeatNumber().size() : 1;
                ticketsByMovie.merge(movieTitle, ticketCount, Integer::sum);
            }
        }
        return ticketsByMovie.entrySet().stream()
                .map(e -> DashboardResponse.MovieTicketSales.builder().movie(e.getKey()).tickets(e.getValue()).build())
                .collect(Collectors.toList());
    }

    @Override
    public List<DashboardResponse.PeakBookingHour> getPeakBookingHours() {
        Map<String, Integer> bookingsByHour = new TreeMap<>();
        for (Ticket t : getTicketsThisWeek()) {
            if (t.getShowTime() != null && t.getShowTime().getTimestamp() != null) {
                Instant instant = Instant.ofEpochMilli(t.getShowTime().getTimestamp());
                ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
                String hour = String.format("%02d:00", zdt.getHour());
                bookingsByHour.merge(hour, 1, Integer::sum);
            }
        }
        return bookingsByHour.entrySet().stream()
                .map(e -> DashboardResponse.PeakBookingHour.builder().hour(e.getKey()).bookings(e.getValue()).build())
                .collect(Collectors.toList());
    }

    @Override
    public List<DashboardResponse.CustomerTypeCount> getCustomerType() {
        int registered = 0, guest = 0;
        for (Ticket t : getTicketsThisWeek()) {
            if (t.getUser() != null && t.getUser().getRole() != null) {
                if (t.getUser().getRole().name().equalsIgnoreCase("GUEST"))
                    guest++;
                else
                    registered++;
            }
        }
        return List.of(
                DashboardResponse.CustomerTypeCount.builder().type("Registered").count(registered).build(),
                DashboardResponse.CustomerTypeCount.builder().type("Guest").count(guest).build());
    }
}
