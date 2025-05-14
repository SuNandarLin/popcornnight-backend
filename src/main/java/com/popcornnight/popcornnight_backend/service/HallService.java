package com.popcornnight.popcornnight_backend.service;

import com.popcornnight.popcornnight_backend.entity.Hall;

import java.util.List;

public interface HallService {
    List<Hall> getAllHalls();

    Hall getHallById(Long hallId);

    Hall createHall(Hall hall);

    Hall updateHall(Long hallId, Hall hall);

    void deleteHall(Long hallId);
}