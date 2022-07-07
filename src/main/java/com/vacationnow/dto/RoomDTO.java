package com.vacationnow.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashMap;

@Getter
@Setter
@NoArgsConstructor
public class RoomDTO {

    private HashMap<String, BigDecimal> price = new HashMap<>();

    private HashMap<String, Integer> availableRoom = new HashMap<>();

    private HashMap<String, Integer> beds = new HashMap<>();

    private HashMap<String, Integer> dropDown = new HashMap<>();

    private String guest;

}
