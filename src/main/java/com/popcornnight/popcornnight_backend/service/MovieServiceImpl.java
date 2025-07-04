package com.popcornnight.popcornnight_backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.popcornnight.popcornnight_backend.converter.MovieConverter;
import com.popcornnight.popcornnight_backend.dto.movie.MovieRequest;
import com.popcornnight.popcornnight_backend.dto.movie.MovieResponse;
import com.popcornnight.popcornnight_backend.entity.Movie;
import com.popcornnight.popcornnight_backend.repository.MovieRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private MovieConverter movieConverter;

    @Override
    @Transactional
    public List<MovieResponse> getAllMovies() {
        List<MovieResponse> movieResponseList = movieRepository
                .findAll().stream()
                .map(movieConverter::convertToMovieResponse)
                .collect(Collectors.toList());
        return movieResponseList;
    }

    @Override
    @Transactional
    public MovieResponse getMovieById(Long movieId) {
        return movieRepository.findById(movieId)
                .map(movieConverter::convertToMovieResponse)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found with id " + movieId));
    }

    @Override
    @Transactional
    public MovieResponse createMovie(MovieRequest movieRequest) {
        Movie movie = Movie.builder()
                .title(movieRequest.getTitle())
                .description(movieRequest.getDescription())
                .releaseDate(movieRequest.getReleaseDate())
                .duration(movieRequest.getDuration())
                .build();

        Movie savedMovie = movieRepository.save(movie);

        return movieConverter.convertToMovieResponse(savedMovie);
    }

    @Override
    @Transactional
    public MovieResponse updateMovie(Long movieId, MovieRequest movieRequest) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Movie not found with id " + movieId));

        if (movieRequest.getTitle() != null && !movieRequest.getTitle().isEmpty()) {
            movie.setTitle(movieRequest.getTitle());
        }
        if (movieRequest.getDescription() != null && !movieRequest.getDescription().isEmpty()) {
            movie.setDescription(movieRequest.getDescription());
        }
        if (movieRequest.getReleaseDate() != null) {
            movie.setReleaseDate(movieRequest.getReleaseDate());
        }
        if (movieRequest.getDuration() > 0) {
            movie.setDuration(movieRequest.getDuration());
        }

        Movie updatedMovie = movieRepository.save(movie);

        return movieConverter.convertToMovieResponse(updatedMovie);
    }

    @Override
    @Transactional
    public String deleteMovie(Long movieId) {
        movieRepository.deleteById(movieId);
        return "Success Deleting Movie Id " + movieId;
    }

}
