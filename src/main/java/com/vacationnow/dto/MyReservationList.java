package com.vacationnow.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MyReservationList {
    private List<MyReservationDTO> myReservationDTOList = new ArrayList<>();
}
