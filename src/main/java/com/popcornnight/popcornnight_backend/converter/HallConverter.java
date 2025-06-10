package com.popcornnight.popcornnight_backend.converter;

import org.springframework.stereotype.Component;

import com.popcornnight.popcornnight_backend.dto.hall.HallResponse;
import com.popcornnight.popcornnight_backend.entity.Hall;

@Component
public class HallConverter {

    public HallResponse convertToHallResponse(Hall hall) {
        // List<ShowTimeResponse> showtimeResponses = hall.getShowTimes().stream()
        // .map(showtimeConverter::convertToShowTimeResponse)
        // .collect(Collectors.toList());

        return HallResponse.builder()
                .id(hall.getId())
                .hallNumber(hall.getHallNumber())
                .totalSeats(hall.getTotalSeats())
                .status(hall.getStatus())
                .seatNoGrid(hall.getSeatNoGrid())
                // .showTimes(showtimeResponses)
                .build();
    }
}
