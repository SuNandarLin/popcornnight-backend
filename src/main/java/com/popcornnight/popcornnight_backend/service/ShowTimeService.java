package com.popcornnight.popcornnight_backend.service;

import com.popcornnight.popcornnight_backend.entity.ShowTime;

import java.util.List;

public interface ShowTimeService {
    List<ShowTime> getAllShowTimes();

    ShowTime getShowTimeById(Long showTimeId);

    ShowTime createShowTime(ShowTime showTime);

    ShowTime updateShowTime(Long showTimeId, ShowTime showTime);

    void deleteShowTime(Long showTimeId);
}