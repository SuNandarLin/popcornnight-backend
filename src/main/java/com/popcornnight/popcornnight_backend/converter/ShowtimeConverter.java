package com.popcornnight.popcornnight_backend.converter;

import org.springframework.stereotype.Component;

import com.popcornnight.popcornnight_backend.dto.showtime.ShowTimeResponse;
import com.popcornnight.popcornnight_backend.entity.ShowTime;

@Component
public class ShowtimeConverter {

    private final MovieConverter movieConverter;
    private final HallConverter hallConverter;

    public ShowtimeConverter(MovieConverter movieConverter, HallConverter hallConverter) {
        this.movieConverter = movieConverter;
        this.hallConverter = hallConverter;

    }

    public ShowTimeResponse convertToShowTimeResponse(ShowTime showTime) {
        return ShowTimeResponse.builder()
                .id(showTime.getId())
                .timeslot(showTime.getTimeslot())
                .timestamp(showTime.getTimestamp())
                .isPublished(showTime.getIsPublished())
                .seatStatusGrid(showTime.getSeatStatusGrid())
                .price(showTime.getPrice())
                .movie(movieConverter.convertToMovieResponse(showTime.getMovie()))
                .hall(hallConverter.convertToHallResponse(showTime.getHall()))
                .build();
    }
}
