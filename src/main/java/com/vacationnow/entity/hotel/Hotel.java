package com.vacationnow.entity.hotel;

import com.vacationnow.entity.ImageUri;
import com.vacationnow.entity.location.Address;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Address address;

    @Column(nullable = false)
    private int stars;

    @Column(nullable = false)
    @Email
    private String email;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private Set<Room> rooms;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<ImageUri> imageUriSet;

    @Column(nullable = false)
    private LocalTime earliestCheckInTime;

    @Column(nullable = false)
    private LocalTime latestCheckInTime;

    @Column(nullable = false)
    private LocalTime checkOutTime;

    @Column(nullable = false)
    private BigDecimal lateCheckoutFee;

    private final static LocalTime DEFAULT_EARLIEST_CHECK_IN = LocalTime.of(13, 0);
    private final static LocalTime DEFAULT_LATEST_CHECK_IN = LocalTime.of(22, 0);
    private final static LocalTime DEFAULT_CHECKOUT = LocalTime.of(11, 0);
    private final static BigDecimal DEFAULT_LATE_CHECKOUT_FEE = BigDecimal.valueOf(15.95);

    public Hotel(String name, Address address, int stars, String email) {
        this(name, address, stars, email,
                DEFAULT_EARLIEST_CHECK_IN,
                DEFAULT_LATEST_CHECK_IN,
                DEFAULT_CHECKOUT,
                DEFAULT_LATE_CHECKOUT_FEE);
    }

    public Hotel(String name, Address address, int stars, String email,
                 LocalTime earliestCheckInTime,
                 LocalTime latestCheckInTime,
                 LocalTime checkOutTime,
                 BigDecimal lateCheckoutFee) {
        this.name = name;
        this.address = address;
        this.stars = stars;
        this.email = email;
        this.rooms = new HashSet<>();
        this.imageUriSet = new ArrayList<>();
        this.earliestCheckInTime = earliestCheckInTime;
        this.latestCheckInTime = latestCheckInTime;
        this.checkOutTime = checkOutTime;
        this.lateCheckoutFee = lateCheckoutFee;
    }

    public void addRoom(Room room) {
        rooms.add(room);
        room.setHotel(this);
    }

    public void addImage(ImageUri uri) {
        imageUriSet.add(uri);
        uri.setHotel(this);
    }
}
