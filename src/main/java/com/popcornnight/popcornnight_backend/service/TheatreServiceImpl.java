package com.popcornnight.popcornnight_backend.service;

import com.popcornnight.popcornnight_backend.dto.hall.HallResponse;
import com.popcornnight.popcornnight_backend.dto.theatre.TheatreRequest;
import com.popcornnight.popcornnight_backend.dto.theatre.TheatreResponse;
import com.popcornnight.popcornnight_backend.entity.Theatre;
import com.popcornnight.popcornnight_backend.repository.TheatreRepository;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TheatreServiceImpl implements TheatreService {

    private final TheatreRepository theatreRepository;
    private final HallService hallService;

    @Override
    public List<TheatreResponse> getAllTheatresWithDetails() {
        return theatreRepository.findAllWithHallsAndShowtimes().stream()
                .map(this::convertToTheatreResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TheatreResponse> getAllTheatres() {
        return theatreRepository.findAll().stream()
                .map(this::convertToTheatreResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TheatreResponse getTheatreById(Long theatreId) {
        Theatre theatre = theatreRepository.findById(theatreId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Theatre not found with id " + theatreId));
        return convertToTheatreResponse(theatre);
    }

    @Override
    public TheatreResponse createTheatre(TheatreRequest theatreRequest) {
        Theatre theatre = Theatre.builder()
                .name(theatreRequest.getName())
                .branch(theatreRequest.getBranch())
                .location(theatreRequest.getLocation())
                .build();
        Theatre savedTheatre = theatreRepository.save(theatre);
        return convertToTheatreResponse(savedTheatre);
    }

    @Override
    public TheatreResponse updateTheatre(Long theatreId, TheatreRequest theatreRequest) {
        Theatre existingTheatre = theatreRepository.findById(theatreId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Theatre not found with id " + theatreId));

        if (theatreRequest.getName() != null && !theatreRequest.getName().isEmpty()) {
            existingTheatre.setName(theatreRequest.getName());
        }
        if (theatreRequest.getBranch() != null && !theatreRequest.getBranch().isEmpty()) {
            existingTheatre.setBranch(theatreRequest.getBranch());
        }
        if (theatreRequest.getLocation() != null && !theatreRequest.getLocation().isEmpty()) {
            existingTheatre.setLocation(theatreRequest.getLocation());
        }
        Theatre updatedTheatre = theatreRepository.save(existingTheatre);
        return convertToTheatreResponse(updatedTheatre);
    }

    @Override
    public void deleteTheatre(Long theatreId) {
        theatreRepository.deleteById(theatreId);
    }

    private TheatreResponse convertToTheatreResponse(Theatre theatre) {

        List<HallResponse> hallResponses = theatre.getHalls().stream()
                .map(hall -> hallService.convertToHallResponse(hall))
                .collect(Collectors.toList());

        return TheatreResponse.builder()
                .id(theatre.getId())
                .name(theatre.getName())
                .branch(theatre.getBranch())
                .location(theatre.getLocation())
                .halls(hallResponses)
                .build();
    }
}