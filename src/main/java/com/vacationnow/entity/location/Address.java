package com.vacationnow.entity.location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Embeddable
@NoArgsConstructor
@Getter
@Setter
public class Address {

    @Column
    @NotEmpty
    @NotNull
    private String streetLine1;

    private String streetLine2;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column
    @Embedded
    @Valid
    private Postcode postcode;

    public Address(String streetLine1, String streetLine2, String city,
                   State state, Postcode postcode,
                   Double latitude, Double longitude) {
        this.streetLine1 = streetLine1;
        this.streetLine2 = streetLine2;
        this.city = city;
        this.state = state;
        this.postcode = postcode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
