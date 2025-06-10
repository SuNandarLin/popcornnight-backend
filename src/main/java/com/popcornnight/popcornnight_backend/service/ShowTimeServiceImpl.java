package com.popcornnight.popcornnight_backend.service;

import com.popcornnight.popcornnight_backend.converter.ShowtimeConverter;
import com.popcornnight.popcornnight_backend.dto.showtime.ShowTimeRequest;
import com.popcornnight.popcornnight_backend.dto.showtime.ShowTimeRequests;
import com.popcornnight.popcornnight_backend.dto.showtime.ShowTimeResponse;
import com.popcornnight.popcornnight_backend.entity.Hall;
import com.popcornnight.popcornnight_backend.entity.Movie;
import com.popcornnight.popcornnight_backend.entity.ShowTime;
import com.popcornnight.popcornnight_backend.repository.HallRepository;
import com.popcornnight.popcornnight_backend.repository.MovieRepository;
import com.popcornnight.popcornnight_backend.repository.ShowTimeRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ShowTimeServiceImpl implements ShowTimeService {

    private final ShowTimeRepository showTimeRepository;
    private final MovieRepository movieRepository;
    private final HallRepository hallRepository;
    private ShowtimeConverter showtimeConverter;

    @Override
    @Transactional
    public List<ShowTimeResponse> createMultipleShowTimes(ShowTimeRequests showTimeRequests) {
        List<ShowTime> entities = showTimeRequests.getShowtimes().stream()
                .map(req -> {
                    ShowTime entity = new ShowTime();
                    entity.setTimestamp(req.getTimestamp());
                    entity.setTimeslot(req.getTimeslot());
                    entity.setSeatStatusGrid(req.getSeatStatusGrid());
                    entity.setIsPublished(req.getIsPublished());

                    Movie movie = movieRepository.findById(req.getMovieId())
                            .orElseThrow(() -> new RuntimeException("Movie not found with ID: " + req.getMovieId()));
                    Hall hall = hallRepository.findById(req.getHallId())
                            .orElseThrow(() -> new RuntimeException("Hall not found with ID: " + req.getHallId()));

                    entity.setMovie(movie);
                    entity.setHall(hall);

                    return entity;
                })
                .collect(Collectors.toList());

        List<ShowTime> savedEntities = showTimeRepository.saveAll(entities);

        List<ShowTimeResponse> responses = savedEntities.stream()
                .map(showtimeConverter::convertToShowTimeResponse)
                .collect(Collectors.toList());

        return responses;
    }

    public List<ShowTimeResponse> getShowTimesByDate(LocalDate date) {
        ZoneId zone = ZoneId.systemDefault();
        long start = date.atStartOfDay(zone).toInstant().toEpochMilli();
        long end = date.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli();

        List<ShowTime> showtimes = showTimeRepository.findByTimestampBetween(start, end);

        List<ShowTimeResponse> responses = showtimes.stream()
                .map(showtimeConverter::convertToShowTimeResponse)
                .collect(Collectors.toList());

        return responses;
    }

    @Override
    public List<ShowTimeResponse> getAllShowTimes() {
        return showTimeRepository.findAll().stream()
                .map(showtimeConverter::convertToShowTimeResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ShowTimeResponse getShowTimeById(Long showTimeId) {
        ShowTime showTime = showTimeRepository.findById(showTimeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "ShowTime not found with id " + showTimeId));
        return showtimeConverter.convertToShowTimeResponse(showTime);
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
        return showtimeConverter.convertToShowTimeResponse(savedShowTime);
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
        return showtimeConverter.convertToShowTimeResponse(updatedShowTime);
    }

    @Override
    public void deleteShowTime(Long showTimeId) {
        showTimeRepository.deleteById(showTimeId);
    }

    @Override
    public void deleteShowtimes(@RequestBody List<Long> ids) {
        showTimeRepository.deleteAllById(ids);
    }

}