package com.vacationnow.entity;

import com.vacationnow.entity.hotel.Hotel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class ImageUri {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String imageUri;

    @ManyToOne
    private Hotel hotel;

    public ImageUri() {
    }

    public ImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

}
