package com.popcornnight.popcornnight_backend.dto.ticket;

import java.util.List;

import com.popcornnight.popcornnight_backend.dto.user.USER_ROLE;

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
    private List<String> seatNumbers;
    private USER_ROLE userRole;
    private Long userId;
    private Long showTimeId;
    private TICKET_STATUS status;
    private String qrId;
}