package com.popcornnight.popcornnight_backend.converter;

import org.springframework.stereotype.Component;

import com.popcornnight.popcornnight_backend.dto.movie.MovieResponse;
import com.popcornnight.popcornnight_backend.entity.Movie;

@Component
public class MovieConverter {
    public MovieResponse convertToMovieResponse(Movie movie) {
        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .description(movie.getDescription())
                .releaseDate(movie.getReleaseDate())
                .duration(movie.getDuration())
                .build();
    }
}
