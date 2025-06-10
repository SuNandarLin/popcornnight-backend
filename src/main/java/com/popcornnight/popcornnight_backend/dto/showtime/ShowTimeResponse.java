package com.popcornnight.popcornnight_backend.dto.showtime;

import java.util.List;

import com.popcornnight.popcornnight_backend.dto.hall.HallResponse;
import com.popcornnight.popcornnight_backend.dto.movie.MovieResponse;

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
    private Long timestamp;
    private Boolean isPublished;
    private List<List<Integer>> seatStatusGrid;
    private MovieResponse movie;
    private HallResponse hall;
    private Long movieId;
    private Long hallId;
}
