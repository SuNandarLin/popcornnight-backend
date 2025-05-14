package com.popcornnight.popcornnight_backend.dto.ticket;

import com.popcornnight.popcornnight_backend.entity.ShowTime;
import com.popcornnight.popcornnight_backend.entity.User;

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
    private String status;
    private Float price;
    private String qrcodeUrl;
    private User user;
    private ShowTime showTime;
}
