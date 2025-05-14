package com.popcornnight.popcornnight_backend.service;

import com.popcornnight.popcornnight_backend.entity.ShowTime;
import com.popcornnight.popcornnight_backend.repository.ShowTimeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ShowTimeServiceImpl implements ShowTimeService {

    private final ShowTimeRepository showTimeRepository;

    @Override
    public List<ShowTime> getAllShowTimes() {
        return showTimeRepository.findAll();
    }

    @Override
    public ShowTime getShowTimeById(Long showTimeId) {
        return showTimeRepository.findById(showTimeId)
                .orElseThrow(() -> new RuntimeException("ShowTime not found with id " + showTimeId));
    }

    @Override
    public ShowTime createShowTime(ShowTime showTime) {
        return showTimeRepository.save(showTime);
    }

    @Override
    public ShowTime updateShowTime(Long showTimeId, ShowTime showTime) {
        ShowTime existingShowTime = getShowTimeById(showTimeId);
        // existingShowTime.setStartTime(showTime.getStartTime());
        // existingShowTime.setEndTime(showTime.getEndTime());
        return showTimeRepository.save(existingShowTime);
    }

    @Override
    public void deleteShowTime(Long showTimeId) {
        showTimeRepository.deleteById(showTimeId);
    }
}