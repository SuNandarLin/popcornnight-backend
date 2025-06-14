package com.popcornnight.popcornnight_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.popcornnight.popcornnight_backend.dto.ticket.TICKET_STATUS;

import jakarta.persistence.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "seat_number")
    private String seatNumber;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TICKET_STATUS status; // enum or string

    @Column(name = "price")
    private Float price;

    @Column(name = "qrcode_url")
    private String qrcodeUrl;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "showtime_id", nullable = false, referencedColumnName = "id")
    private ShowTime showTime;
}
