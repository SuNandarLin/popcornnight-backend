package com.popcornnight.popcornnight_backend.dto.showtime;

import java.util.List;

import com.popcornnight.popcornnight_backend.entity.Hall;
import com.popcornnight.popcornnight_backend.entity.Movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShowTimeResponse {
    private long id;
    private String timeslot;
    private Integer timestamp;
    private Boolean isPublished;
    private List<List<Integer>> seatStatusGrid;
    private Movie movie;
    private Hall hall;
}
