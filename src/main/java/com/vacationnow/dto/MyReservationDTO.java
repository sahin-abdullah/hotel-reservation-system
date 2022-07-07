package com.vacationnow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class MyReservationDTO {
    private Long id;
    private String hotelName;
    private Integer hotelStar;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDate creationDate;
}
