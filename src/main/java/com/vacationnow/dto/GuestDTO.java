package com.vacationnow.dto;

import com.vacationnow.entity.guest.Guest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GuestDTO {
    private List<Guest> guestList = new ArrayList<>();
}
