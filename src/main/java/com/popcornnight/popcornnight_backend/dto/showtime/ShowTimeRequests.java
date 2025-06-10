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
public class ShowTimeRequests {
    private List<ShowTimeRequest> showtimes;
}
