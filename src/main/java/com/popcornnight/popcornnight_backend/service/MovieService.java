package com.popcornnight.popcornnight_backend.service;

import java.util.List;

import com.popcornnight.popcornnight_backend.dto.movie.MovieRequest;
import com.popcornnight.popcornnight_backend.dto.movie.MovieResponse;

public interface MovieService {

    List<MovieResponse> getAllMovies();

    MovieResponse getMovieById(Long movieId);

    MovieResponse createMovie(MovieRequest movieRequest);

    MovieResponse updateMovie(Long movieId, MovieRequest movieRequest);

    String deleteMovie(Long movieId);

}
