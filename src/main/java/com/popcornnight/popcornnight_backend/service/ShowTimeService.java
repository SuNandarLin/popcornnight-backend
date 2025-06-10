package com.popcornnight.popcornnight_backend.service;

import com.popcornnight.popcornnight_backend.dto.showtime.ShowTimeRequest;
import com.popcornnight.popcornnight_backend.dto.showtime.ShowTimeRequests;
import com.popcornnight.popcornnight_backend.dto.showtime.ShowTimeResponse;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

public interface ShowTimeService {
    List<ShowTimeResponse> createMultipleShowTimes(ShowTimeRequests showTimeRequests);

    List<ShowTimeResponse> getShowTimesByDate(LocalDate date);

    List<ShowTimeResponse> getAllShowTimes();

    ShowTimeResponse getShowTimeById(Long showTimeId);

    ShowTimeResponse createShowTime(ShowTimeRequest showTimeRequest);

    ShowTimeResponse updateShowTime(Long showTimeId, ShowTimeRequest showTimeRequest);

    void deleteShowTime(Long showTimeId);

    void deleteShowtimes(@RequestBody List<Long> ids);

}