package com.popcornnight.popcornnight_backend.controller;

import com.popcornnight.popcornnight_backend.dto.hall.HallRequest;
import com.popcornnight.popcornnight_backend.dto.hall.HallResponse;
import com.popcornnight.popcornnight_backend.service.HallService;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/halls")
@AllArgsConstructor
public class HallController {

    private final HallService hallService;

    @GetMapping
    public List<HallResponse> getAllHalls() {
        return hallService.getAllHalls();
    }

    @GetMapping("/{id}")
    public HallResponse getHallById(@PathVariable Long id) {
        return hallService.getHallById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HallResponse createHall(@RequestBody HallRequest hallRequest) {
        return hallService.createHall(hallRequest);
    }

    @PutMapping("/{id}")
    public HallResponse updateHall(@PathVariable Long id, @RequestBody HallRequest hallRequest) {
        return hallService.updateHall(id, hallRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHall(@PathVariable Long id) {
        hallService.deleteHall(id);
    }
}