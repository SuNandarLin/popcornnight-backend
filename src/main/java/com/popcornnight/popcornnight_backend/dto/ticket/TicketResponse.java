package com.popcornnight.popcornnight_backend.dto.ticket;

import java.util.List;

import com.popcornnight.popcornnight_backend.dto.showtime.ShowTimeResponse;
import com.popcornnight.popcornnight_backend.dto.user.UserResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {
    private long id;
    private List<String> seatNumbers;
    private TICKET_STATUS status;
    private Float price;
    private String qrcodeUrl;
    private UserResponse user;
    private ShowTimeResponse showTime;
    private long userId;
    private long showTimeId;
    private String qrId;
}
