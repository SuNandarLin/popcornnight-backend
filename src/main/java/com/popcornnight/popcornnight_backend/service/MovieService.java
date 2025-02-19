package com.popcornnight.popcornnight_backend.service;

import com.popcornnight.popcornnight_backend.dto.MovieRequest;
import com.popcornnight.popcornnight_backend.dto.MovieResponse;

import java.util.List;

public interface MovieService {

    List<MovieResponse> getAllMovies();

    MovieResponse getMovieById(Long movieId);

    MovieResponse createMovie(MovieRequest movieRequest);

    MovieResponse updateMovie(Long movieId, MovieRequest movieRequest);

    String deleteMovie(Long movieId);

}
