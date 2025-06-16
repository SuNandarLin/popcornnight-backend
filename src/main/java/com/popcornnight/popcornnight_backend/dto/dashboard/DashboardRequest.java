package com.popcornnight.popcornnight_backend.dto.dashboard;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DashboardRequest {
    private LocalDate startDate;
    private LocalDate endDate;
    private Long movieId;
}
