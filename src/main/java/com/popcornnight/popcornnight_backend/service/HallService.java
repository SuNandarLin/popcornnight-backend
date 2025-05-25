package com.popcornnight.popcornnight_backend.service;

import com.popcornnight.popcornnight_backend.dto.hall.HallRequest;
import com.popcornnight.popcornnight_backend.dto.hall.HallResponse;
import com.popcornnight.popcornnight_backend.entity.Hall;

import java.util.List;

public interface HallService {
    List<HallResponse> getAllHalls();

    HallResponse getHallById(Long hallId);

    HallResponse createHall(HallRequest hallRequest);

    HallResponse updateHall(Long hallId, HallRequest hallRequest);

    void deleteHall(Long hallId);

    HallResponse convertToHallResponse(Hall hall);
}