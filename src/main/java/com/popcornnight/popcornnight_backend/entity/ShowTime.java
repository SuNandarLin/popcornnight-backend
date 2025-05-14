package com.popcornnight.popcornnight_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "show_times")
public class ShowTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "timeslot")
    private String timeslot;

    @Column(name = "timestamp")
    private Integer timestamp;

    @Column(name = "is_published")
    private Boolean isPublished;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "seatstatus_grid", columnDefinition = "json")
    private List<List<Integer>> seatStatusGrid; // e.g., [[0, 0], [0, 0]]

    @ManyToOne(optional = false)
    @JoinColumn(name = "movie_id", nullable = false, referencedColumnName = "id")
    private Movie movie;

    @ManyToOne(optional = false)
    @JoinColumn(name = "hall_id", nullable = false, referencedColumnName = "id")
    private Hall hall;

    @OneToMany(mappedBy = "showTime", cascade = CascadeType.MERGE)
    private Set<Ticket> tickets;
}
