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
public class TicketQRcodeInfo {
    private String qrId;
    private long ticketId;
    private TICKET_STATUS ticketStatus;
    private String seatNumber;
    private String movieTitle;
    private String showTimeslot;
    private Long geneartedAt;
}
