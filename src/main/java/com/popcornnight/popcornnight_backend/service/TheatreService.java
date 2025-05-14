package com.popcornnight.popcornnight_backend.service;

import com.popcornnight.popcornnight_backend.entity.Theatre;

import java.util.List;

public interface TheatreService {
    List<Theatre> getAllTheatres();

    Theatre getTheatreById(Long theatreId);

    Theatre createTheatre(Theatre theatre);

    Theatre updateTheatre(Long theatreId, Theatre theatre);

    void deleteTheatre(Long theatreId);
}