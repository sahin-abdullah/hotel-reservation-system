package com.vacationnow.entity.hotel;

import com.vacationnow.entity.reservation.Reservation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Hotel hotel;

    @NaturalId
    @Column(nullable = false, unique = true)
    private String roomNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    @Column(nullable = false)
    private int beds;

    @Column(nullable = false)
    private BigDecimal costPerNight;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Reservation> reservation;

    public Room(String roomNumber, RoomType roomType, int beds, BigDecimal costPerNight) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.beds = beds;
        this.costPerNight = costPerNight;
    }
}
