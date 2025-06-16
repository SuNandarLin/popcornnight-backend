package com.popcornnight.popcornnight_backend.dto.showtime;

import java.util.List;

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
public class ShowTimeRequest {
    private String timeslot;
    private Long timestamp;
    private Boolean isPublished;
    private List<List<Integer>> seatStatusGrid;
    private Float price;
    private Long movieId;
    private Long hallId;
}
