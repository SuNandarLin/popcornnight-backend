package com.popcornnight.popcornnight_backend.controller;

import com.popcornnight.popcornnight_backend.dto.showtime.ShowTimeRequest;
import com.popcornnight.popcornnight_backend.dto.showtime.ShowTimeResponse;
import com.popcornnight.popcornnight_backend.service.ShowTimeService;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/showtimes")
@AllArgsConstructor
public class ShowTimeController {

    private final ShowTimeService showTimeService;

    @GetMapping
    public List<ShowTimeResponse> getAllShowTimes() {
        return showTimeService.getAllShowTimes();
    }

    @GetMapping("/{id}")
    public ShowTimeResponse getShowTimeById(@PathVariable Long id) {
        return showTimeService.getShowTimeById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShowTimeResponse createShowTime(@RequestBody ShowTimeRequest showTimeRequest) {
        return showTimeService.createShowTime(showTimeRequest);
    }

    @PutMapping("/{id}")
    public ShowTimeResponse updateShowTime(@PathVariable Long id, @RequestBody ShowTimeRequest showTimeRequest) {
        return showTimeService.updateShowTime(id, showTimeRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteShowTime(@PathVariable Long id) {
        showTimeService.deleteShowTime(id);
    }
}