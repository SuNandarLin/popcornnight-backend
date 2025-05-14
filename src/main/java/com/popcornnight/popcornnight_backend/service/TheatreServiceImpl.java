package com.popcornnight.popcornnight_backend.service;

import com.popcornnight.popcornnight_backend.entity.Theatre;
import com.popcornnight.popcornnight_backend.repository.TheatreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TheatreServiceImpl implements TheatreService {

    private final TheatreRepository theatreRepository;

    @Override
    public List<Theatre> getAllTheatres() {
        return theatreRepository.findAll();
    }

    @Override
    public Theatre getTheatreById(Long theatreId) {
        return theatreRepository.findById(theatreId)
                .orElseThrow(() -> new RuntimeException("Theatre not found with id " + theatreId));
    }

    @Override
    public Theatre createTheatre(Theatre theatre) {
        return theatreRepository.save(theatre);
    }

    @Override
    public Theatre updateTheatre(Long theatreId, Theatre theatre) {
        Theatre existingTheatre = getTheatreById(theatreId);
        existingTheatre.setName(theatre.getName());
        existingTheatre.setLocation(theatre.getLocation());
        return theatreRepository.save(existingTheatre);
    }

    @Override
    public void deleteTheatre(Long theatreId) {
        theatreRepository.deleteById(theatreId);
    }
}