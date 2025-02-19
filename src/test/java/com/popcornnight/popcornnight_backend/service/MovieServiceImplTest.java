package com.popcornnight.popcornnight_backend.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.popcornnight.popcornnight_backend.dto.MovieRequest;
import com.popcornnight.popcornnight_backend.dto.MovieResponse;
import com.popcornnight.popcornnight_backend.entity.Movie;
import com.popcornnight.popcornnight_backend.repository.MovieRepository;

@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieServiceImpl movieService;

    private MovieRequest movieRequest;
    // private MovieResponse movieResponse;
    private Movie movie;

    @BeforeEach
    void setUp() {
        movieRequest = MovieRequest.builder()
                .title("Love Dead and Robots")
                .description("Series of short stories sci-fi")
                .duration(120)
                .releaseDate(java.sql.Date.valueOf("2014-11-11"))
                .build();

        // movieResponse = MovieResponse.builder()
        // .id(1L)
        // .title("Love Dead and Robots")
        // .description("Series of short stories sci-fi")
        // .duration(120)
        // .releaseDate(java.sql.Date.valueOf("2014-11-11"))
        // .build();

        movie = Movie.builder()
                .id(1L)
                .title("Love Dead and Robots")
                .description("Series of short stories sci-fi")
                .duration(120)
                .releaseDate(java.sql.Date.valueOf("2014-11-11"))
                .build();
    }

    @Test
    void testGetAllMovies() {
        when(movieRepository.findAll()).thenReturn(List.of(movie));

        List<MovieResponse> movies = movieService.getAllMovies();

        assertEquals(1, movies.size());
        assertEquals("Love Dead and Robots", movies.get(0).getTitle());
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    void testGetMovieById_Found() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        MovieResponse response = movieService.getMovieById(1L);

        assertNotNull(response);
        assertEquals("Love Dead and Robots", response.getTitle());
        verify(movieRepository, times(1)).findById(1L);
    }

    @Test
    void testGetMovieById_NotFound() {
        when(movieRepository.findById(2L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            movieService.getMovieById(2L);
        });

        assertEquals("404 NOT_FOUND \"Movie not found with id 2\"", exception.getMessage());
    }

    @Test
    void testCreateMovie() {
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        MovieResponse response = movieService.createMovie(movieRequest);

        assertNotNull(response);
        assertEquals("Love Dead and Robots", response.getTitle());
        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    @Test
    void testUpdateMovie_Found() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        MovieRequest updatedRequest = MovieRequest.builder()
                .title("Interstellar")
                .description("A space adventure")
                .releaseDate(java.sql.Date.valueOf("2014-10-10"))
                .duration(169)
                .build();

        MovieResponse response = movieService.updateMovie(1L, updatedRequest);

        assertNotNull(response);
        assertEquals("Interstellar", response.getTitle());
        assertEquals("A space adventure", response.getDescription());
        verify(movieRepository, times(1)).findById(1L);
        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    @Test
    void testUpdateMovie_NotFound() {
        when(movieRepository.findById(2L)).thenReturn(Optional.empty());

        MovieRequest updatedRequest = MovieRequest.builder()
                .title("Interstellar")
                .description("A space adventure")
                .releaseDate(java.sql.Date.valueOf("2014-10-10"))
                .duration(169)
                .build();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            movieService.updateMovie(2L, updatedRequest);
        });

        assertEquals("400 BAD_REQUEST \"Movie not found with id 2\"", exception.getMessage());
    }

    @Test
    void testDeleteMovie() {
        doNothing().when(movieRepository).deleteById(1L);

        String response = movieService.deleteMovie(1L);

        assertEquals("Success Deleting Movie Id 1", response);
        verify(movieRepository, times(1)).deleteById(1L);
    }
}
