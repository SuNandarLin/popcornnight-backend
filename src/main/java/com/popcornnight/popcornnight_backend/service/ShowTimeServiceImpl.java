package com.popcornnight.popcornnight_backend.service;

import com.popcornnight.popcornnight_backend.dto.showtime.ShowTimeRequest;
import com.popcornnight.popcornnight_backend.dto.showtime.ShowTimeResponse;
import com.popcornnight.popcornnight_backend.entity.Hall;
import com.popcornnight.popcornnight_backend.entity.Movie;
import com.popcornnight.popcornnight_backend.entity.ShowTime;
import com.popcornnight.popcornnight_backend.repository.HallRepository;
import com.popcornnight.popcornnight_backend.repository.MovieRepository;
import com.popcornnight.popcornnight_backend.repository.ShowTimeRepository;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ShowTimeServiceImpl implements ShowTimeService {

    private final ShowTimeRepository showTimeRepository;
    private final MovieRepository movieRepository;
    private final HallRepository hallRepository;

    @Override
    public List<ShowTimeResponse> getAllShowTimes() {
        return showTimeRepository.findAll().stream()
                .map(this::convertToShowTimeResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ShowTimeResponse getShowTimeById(Long showTimeId) {
        ShowTime showTime = showTimeRepository.findById(showTimeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "ShowTime not found with id " + showTimeId));
        return convertToShowTimeResponse(showTime);
    }

    @Override
    public ShowTimeResponse createShowTime(ShowTimeRequest showTimeRequest) {
        Hall hall = hallRepository.findById(showTimeRequest.getHallId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Hall not found with id " + showTimeRequest.getHallId()));
        Movie movie = movieRepository.findById(showTimeRequest.getHallId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Movie not found with id " + showTimeRequest.getHallId()));

        ShowTime showTime = ShowTime.builder()
                .timeslot(showTimeRequest.getTimeslot())
                .timestamp(showTimeRequest.getTimestamp())
                .isPublished(showTimeRequest.getIsPublished())
                .movie(movie)
                .hall(hall)
                .build();

        ShowTime savedShowTime = showTimeRepository.save(showTime);
        return convertToShowTimeResponse(savedShowTime);
    }

    @Override
    public ShowTimeResponse updateShowTime(Long showTimeId, ShowTimeRequest showTimeRequest) {
        ShowTime existingShowTime = showTimeRepository.findById(showTimeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Showtime not found with id " + showTimeId));

        if (showTimeRequest.getTimeslot() != null && !showTimeRequest.getTimeslot().isEmpty()) {
            existingShowTime.setTimeslot(showTimeRequest.getTimeslot());
        }
        if (showTimeRequest.getTimestamp() != null && showTimeRequest.getTimestamp() > 0) {
            existingShowTime.setTimestamp(showTimeRequest.getTimestamp());
        }
        if (showTimeRequest.getIsPublished() != null) {
            existingShowTime.setIsPublished(showTimeRequest.getIsPublished());
        }
        if (showTimeRequest.getSeatStatusGrid() != null && !showTimeRequest.getSeatStatusGrid().isEmpty()) {
            existingShowTime.setSeatStatusGrid(showTimeRequest.getSeatStatusGrid());
        }
        if (showTimeRequest.getMovieId() != null && showTimeRequest.getMovieId() > 0) {
            Movie movie = movieRepository.findById(showTimeRequest.getMovieId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Movie not found with id " + showTimeRequest.getMovieId()));

            existingShowTime.setMovie(movie);
        }
        if (showTimeRequest.getHallId() != null && showTimeRequest.getHallId() > 0) {
            Hall hall = hallRepository.findById(showTimeRequest.getHallId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Hall not found with id " + showTimeRequest.getHallId()));
            existingShowTime.setHall(hall);
        }
        ShowTime updatedShowTime = showTimeRepository.save(existingShowTime);
        return convertToShowTimeResponse(updatedShowTime);
    }

    @Override
    public void deleteShowTime(Long showTimeId) {
        showTimeRepository.deleteById(showTimeId);
    }

    private ShowTimeResponse convertToShowTimeResponse(ShowTime showTime) {
        return ShowTimeResponse.builder()
                .id(showTime.getId())
                .timeslot(showTime.getTimeslot())
                .timestamp(showTime.getTimestamp())
                .isPublished(showTime.getIsPublished())
                .seatStatusGrid(showTime.getSeatStatusGrid())
                .movie(showTime.getMovie())
                .hall(showTime.getHall())
                .build();
    }
}