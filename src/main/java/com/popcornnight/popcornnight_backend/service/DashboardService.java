package com.popcornnight.popcornnight_backend.service;

import com.popcornnight.popcornnight_backend.dto.dashboard.DashboardResponse;

import java.util.List;

public interface DashboardService {
    int getTotalRevenueThisWeek();

    int getTotalTicketsThisWeek();

    List<DashboardResponse.MovieRevenue> getRevenueByMovie();

    List<DashboardResponse.MovieTicketSales> getMovieTicketSales();

    List<DashboardResponse.PeakBookingHour> getPeakBookingHours();

    List<DashboardResponse.CustomerTypeCount> getCustomerType();
}
