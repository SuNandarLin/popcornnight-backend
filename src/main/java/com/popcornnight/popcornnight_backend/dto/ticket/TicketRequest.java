package com.popcornnight.popcornnight_backend.dto.ticket;

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
public class TicketRequest {
    private String seatNumber;
    private String status;
    private Float price;
    private String qrcodeUrl;
    private Long userId;
    private Long showTimeId;
}
