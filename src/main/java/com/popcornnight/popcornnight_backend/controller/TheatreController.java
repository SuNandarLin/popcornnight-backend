package com.popcornnight.popcornnight_backend.controller;

import com.popcornnight.popcornnight_backend.dto.theatre.TheatreRequest;
import com.popcornnight.popcornnight_backend.dto.theatre.TheatreResponse;
import com.popcornnight.popcornnight_backend.service.TheatreService;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theatres")
@AllArgsConstructor
public class TheatreController {

    private final TheatreService theatreService;

    @GetMapping
    public List<TheatreResponse> getAllTheatres() {
        return theatreService.getAllTheatres();
    }

    @GetMapping("/{id}")
    public TheatreResponse getTheatreById(@PathVariable Long id) {
        return theatreService.getTheatreById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TheatreResponse createTheatre(@RequestBody TheatreRequest theatreRequest) {
        return theatreService.createTheatre(theatreRequest);
    }

    @PutMapping("/{id}")
    public TheatreResponse updateTheatre(@PathVariable Long id, @RequestBody TheatreRequest theatreRequest) {
        return theatreService.updateTheatre(id, theatreRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTheatre(@PathVariable Long id) {
        theatreService.deleteTheatre(id);
    }
}