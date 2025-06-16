package com.popcornnight.popcornnight_backend.controller;

import com.popcornnight.popcornnight_backend.dto.dashboard.DashboardResponse;
import com.popcornnight.popcornnight_backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/total-revenue-this-week")
    public int getTotalRevenueThisWeek() {
        return dashboardService.getTotalRevenueThisWeek();
    }

    @GetMapping("/total-tickets-this-week")
    public int getTotalTicketsThisWeek() {
        return dashboardService.getTotalTicketsThisWeek();
    }

    @GetMapping("/revenue-by-movie")
    public List<DashboardResponse.MovieRevenue> getRevenueByMovie() {
        return dashboardService.getRevenueByMovie();
    }

    @GetMapping("/movie-ticket-sales")
    public List<DashboardResponse.MovieTicketSales> getMovieTicketSales() {
        return dashboardService.getMovieTicketSales();
    }

    @GetMapping("/peak-booking-hours")
    public List<DashboardResponse.PeakBookingHour> getPeakBookingHours() {
        return dashboardService.getPeakBookingHours();
    }

    @GetMapping("/customer-type")
    public List<DashboardResponse.CustomerTypeCount> getCustomerType() {
        return dashboardService.getCustomerType();
    }
}
