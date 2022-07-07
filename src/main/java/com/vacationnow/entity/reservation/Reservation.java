package com.vacationnow.entity.reservation;


import com.vacationnow.entity.guest.Guest;
import com.vacationnow.entity.hotel.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;

    @ManyToMany
    @JoinTable(
            name = "reservation_room",
            joinColumns = @JoinColumn(name = "reservation_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "room_id", referencedColumnName = "id")
    )
    private Set<Room> room = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "reservation_guests",
            joinColumns = @JoinColumn(name = "reservation_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "guest_id", referencedColumnName = "id")
    )
    private Set<Guest> guests = new HashSet<>();

    @Embedded
    @Valid
    private ReservationDates dates = new ReservationDates();

    @Column(nullable = false)
    private LocalDateTime createdTime;

    public Reservation(Set<Room> room, Set<Guest> guests, ReservationDates dates, LocalDateTime createdTime) {
        this.room = room;
        this.guests = guests;
        this.dates = dates;
        this.createdTime = createdTime;
    }
}