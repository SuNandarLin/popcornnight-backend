package com.popcornnight.popcornnight_backend.service;

import com.popcornnight.popcornnight_backend.dto.theatre.TheatreRequest;
import com.popcornnight.popcornnight_backend.dto.theatre.TheatreResponse;

import java.util.List;

public interface TheatreService {
    List<TheatreResponse> getAllTheatres();

    TheatreResponse getTheatreById(Long theatreId);

    TheatreResponse createTheatre(TheatreRequest theatreRequest);

    TheatreResponse updateTheatre(Long theatreId, TheatreRequest theatreRequest);

    void deleteTheatre(Long theatreId);
}