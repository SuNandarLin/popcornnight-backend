package com.popcornnight.popcornnight_backend.service;

import com.popcornnight.popcornnight_backend.entity.Hall;
import com.popcornnight.popcornnight_backend.repository.HallRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class HallServiceImpl implements HallService {

    private final HallRepository hallRepository;

    @Override
    public List<Hall> getAllHalls() {
        return hallRepository.findAll();
    }

    @Override
    public Hall getHallById(Long hallId) {
        return hallRepository.findById(hallId)
                .orElseThrow(() -> new RuntimeException("Hall not found with id " + hallId));
    }

    @Override
    public Hall createHall(Hall hall) {
        return hallRepository.save(hall);
    }

    @Override
    public Hall updateHall(Long hallId, Hall hall) {
        Hall existingHall = getHallById(hallId);
        // existingHall.setName(hall.getName());
        // existingHall.setCapacity(hall.getCapacity());
        return hallRepository.save(existingHall);
    }

    @Override
    public void deleteHall(Long hallId) {
        hallRepository.deleteById(hallId);
    }
}