package com.popcornnight.popcornnight_backend.dto.ticket;

import com.google.firebase.remoteconfig.internal.TemplateResponse.UserResponse;
import com.popcornnight.popcornnight_backend.dto.showtime.ShowTimeResponse;

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
    private String seatNumber;
    private TICKET_STATUS status;
    private Float price;
    private String qrcodeUrl;
    private UserResponse user;
    private ShowTimeResponse showTime;
    private long userId;
    private long showTimeId;
}
