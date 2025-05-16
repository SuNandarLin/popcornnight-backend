package com.popcornnight.popcornnight_backend.service;

import com.popcornnight.popcornnight_backend.dto.showtime.ShowTimeRequest;
import com.popcornnight.popcornnight_backend.dto.showtime.ShowTimeResponse;

import java.util.List;

public interface ShowTimeService {
    List<ShowTimeResponse> getAllShowTimes();

    ShowTimeResponse getShowTimeById(Long showTimeId);

    ShowTimeResponse createShowTime(ShowTimeRequest showTimeRequest);

    ShowTimeResponse updateShowTime(Long showTimeId, ShowTimeRequest showTimeRequest);

    void deleteShowTime(Long showTimeId);
}