package com.popcornnight.popcornnight_backend.dto.hall;

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
public class HallRequest {
    private String hallNumber;
    private Integer totalSeats;
    private String status;
    private List<List<String>> seatNoGrid;
    private Long theatreId;
}
