package com.vacationnow.service;

import com.vacationnow.entity.hotel.Hotel;
import com.vacationnow.entity.hotel.Room;
import com.vacationnow.entity.reservation.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface HotelService {

    List<Hotel> getAllHotels(Integer pageNo, Integer pageSize, String sortBy);

    List<Hotel> getClosestHotels(Double lat, Double lng, Integer pageNo, Integer pageSize);

    List<Room> getAllRooms(Long hotelId);

    List<Room> getReservedRooms(Long hotelId, LocalDate checkIn, LocalDate checkOut);

    Hotel getHotelById(Long hotelId);

    List<Room> reserveRooms(Long hotelId, String roomType, Integer roomNum);

    Reservation saveReservation(Reservation reservation);

    void removeReservation(Long reservationId);

    List<Object[]> findReservationById(Long userId);
}
