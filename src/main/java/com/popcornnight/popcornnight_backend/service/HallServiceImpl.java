package com.popcornnight.popcornnight_backend.service;

import com.popcornnight.popcornnight_backend.dto.hall.HallRequest;
import com.popcornnight.popcornnight_backend.dto.hall.HallResponse;
import com.popcornnight.popcornnight_backend.dto.showtime.ShowTimeResponse;
import com.popcornnight.popcornnight_backend.entity.Hall;
import com.popcornnight.popcornnight_backend.entity.Theatre;
import com.popcornnight.popcornnight_backend.repository.HallRepository;
import com.popcornnight.popcornnight_backend.repository.TheatreRepository;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HallServiceImpl implements HallService {

    private final HallRepository hallRepository;
    private final TheatreRepository theatreRepository;
    private final ShowTimeService showTimeService;

    @Override
    public List<HallResponse> getAllHalls() {
        return hallRepository.findAll().stream()
                .map(this::convertToHallResponse)
                .collect(Collectors.toList());
    }

    @Override
    public HallResponse getHallById(Long hallId) {
        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Hall not found with id " + hallId));
        return convertToHallResponse(hall);
    }

    @Override
    public HallResponse createHall(HallRequest hallRequest) {
        Theatre theatre = theatreRepository.findById(hallRequest.getTheatreId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Theatre not found with id " + hallRequest.getTheatreId()));

        Hall hall = Hall.builder()
                .hallNumber(hallRequest.getHallNumber())
                .totalSeats(hallRequest.getTotalSeats())
                .status(hallRequest.getStatus())
                .seatNoGrid(hallRequest.getSeatNoGrid())
                .theatre(theatre)
                .build();

        Hall savedHall = hallRepository.save(hall);
        return convertToHallResponse(savedHall);
    }

    @Override
    public HallResponse updateHall(Long hallId, HallRequest hallRequest) {
        Hall existingHall = hallRepository.findById(hallId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Hall not found with id " + hallId));

        if (hallRequest.getHallNumber() != null && !hallRequest.getHallNumber().isEmpty()) {
            existingHall.setHallNumber(hallRequest.getHallNumber());
        }
        if (hallRequest.getTotalSeats() != null && hallRequest.getTotalSeats() >= 0) {
            existingHall.setTotalSeats(hallRequest.getTotalSeats());
        }
        if (hallRequest.getStatus() != null && !hallRequest.getStatus().isEmpty()) {
            existingHall.setStatus(hallRequest.getStatus());
        }
        if (hallRequest.getSeatNoGrid() != null && !hallRequest.getSeatNoGrid().isEmpty()) {
            existingHall.setSeatNoGrid(hallRequest.getSeatNoGrid());
        }
        if (hallRequest.getTheatreId() != null && hallRequest.getTheatreId() > 0) {
            Theatre theatre = theatreRepository.findById(hallRequest.getTheatreId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Theatre not found with id " + hallRequest.getTheatreId()));
            existingHall.setTheatre(theatre);
        }
        Hall updatedHall = hallRepository.save(existingHall);
        return convertToHallResponse(updatedHall);
    }

    @Override
    public void deleteHall(Long hallId) {
        hallRepository.deleteById(hallId);
    }

    @Override
    public HallResponse convertToHallResponse(Hall hall) {
        List<ShowTimeResponse> showtimeResponses = hall.getShowTimes().stream()
                .map(showTime -> showTimeService.convertToShowTimeResponse(showTime))
                .collect(Collectors.toList());

        return HallResponse.builder()
                .id(hall.getId())
                .hallNumber(hall.getHallNumber())
                .totalSeats(hall.getTotalSeats())
                .status(hall.getStatus())
                .seatNoGrid(hall.getSeatNoGrid())
                .showTimes(showtimeResponses)
                .build();
    }
}