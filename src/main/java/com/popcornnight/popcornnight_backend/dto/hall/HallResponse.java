package com.popcornnight.popcornnight_backend.dto.hall;

import java.util.List;

import com.popcornnight.popcornnight_backend.entity.Theatre;

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
public class HallResponse {
    private long id;
    private String hallNumber;
    private String totalSeats;
    private String status;
    private List<List<String>> seatNoGrid;
    private Theatre theatre;
}
