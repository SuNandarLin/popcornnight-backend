package com.popcornnight.popcornnight_backend.dto.dashboard;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class DashboardResponse {
    private Integer totalRevenueThisWeek;
    private Integer totalTicketsThisWeek;
    private List<MovieRevenue> revenueByMovie;
    private List<MovieTicketSales> movieTicketSales;
    private List<PeakBookingHour> peakBookingHours;
    private List<CustomerTypeCount> customerType;

    @Data
    @Builder
    public static class MovieRevenue {
        private String movie;
        private Integer revenue;
    }

    @Data
    @Builder
    public static class MovieTicketSales {
        private String movie;
        private Integer tickets;
    }

    @Data
    @Builder
    public static class PeakBookingHour {
        private String hour;
        private Integer bookings;
    }

    @Data
    @Builder
    public static class CustomerTypeCount {
        private String type;
        private Integer count;
    }
}
