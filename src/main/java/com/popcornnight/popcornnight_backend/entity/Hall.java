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
@Table(name = "halls")
public class Hall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "hall_number")
    private String hallNumber;

    @Column(name = "total_seats")
    private Integer totalSeats;

    @Column(name = "status")
    private String status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "seatno_grid", columnDefinition = "json")
    private List<List<String>> seatNoGrid; // Two dimensional array e.g., [["A1", "A2"], ["B1", "B2"]]

    @ManyToOne(optional = false)
    @JoinColumn(name = "theatre_id", nullable = false, referencedColumnName = "id")
    private Theatre theatre;

    @OneToMany(mappedBy = "hall", cascade = CascadeType.MERGE)
    private Set<ShowTime> showTimes;
}
