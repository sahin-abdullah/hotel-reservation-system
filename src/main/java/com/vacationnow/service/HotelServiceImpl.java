package com.vacationnow.service;

import com.vacationnow.entity.hotel.Hotel;
import com.vacationnow.entity.hotel.Room;
import com.vacationnow.entity.hotel.RoomType;
import com.vacationnow.entity.reservation.Reservation;
import com.vacationnow.repository.HotelRepository;
import com.vacationnow.repository.ReservationRepository;
import com.vacationnow.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public List<Hotel> getAllHotels(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Hotel> pagedResult = hotelRepository.findAll(paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Hotel>();
        }
    }

    public Integer getCountAllHotels() {
        return hotelRepository.getCount();
    }

    @Override
    public List<Hotel> getClosestHotels(Double lat, Double lng, Integer pageNo, Integer pageSize) {

        Pageable paging = PageRequest.of(pageNo, pageSize);

        Double sf = 3.14159 / 180;
        Page<Hotel> pagedResult = hotelRepository.findClosestHotel(lat, lng, sf, paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Hotel>();
        }
    }

    @Override
    public List<Room> getAllRooms(Long hotelId) {

//        Pageable paging = PageRequest.of(pageNo, pageSize);

        List<Room> listedResult = roomRepository.findRoomByHotelId(hotelId);
        if(listedResult.isEmpty()) {
            return new ArrayList<Room>();
        } else {
            return listedResult;
        }
    }

    @Override
    public List<Room> getReservedRooms(Long hotelId, LocalDate checkIn, LocalDate checkOut) {
        List<Room> listedResult = roomRepository.findReservedRoom(hotelId, checkIn, checkOut);

        if(listedResult.isEmpty()) {
            return new ArrayList<Room>();
        } else {
            return listedResult;
        }
    }

    @Override
    public Hotel getHotelById(Long hotelId) {
        Optional<Hotel> hotel = hotelRepository.findById(hotelId);
        return hotel.get();
    }

    @Override
    public List<Room> reserveRooms(Long hotelId, String roomType, Integer roomNum) {
        List<Object[]> listedResult = roomRepository.willBeReserved(hotelId, roomType, roomNum);
        if(listedResult.isEmpty()) {
            return new ArrayList<Room>();
        } else {
            List<Room> rooms = new ArrayList<>();
            for (Object[] obj : listedResult) {
                BigInteger id = (BigInteger) obj[0];

                Room myRoom = new Room((String) obj[3], RoomType.valueOf((String) obj[4]), (Integer) obj[1], (BigDecimal) obj[2]);
                myRoom.setId(id.longValue());
                rooms.add(myRoom);
            }
            return rooms;
        }
    }

    @Override
    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public void removeReservation(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }

    @Override
    public List<Object[]> findReservationById(Long userId) {
        List<Object[]> listedResult = reservationRepository.findReservationById(userId);
        if(listedResult.isEmpty()) {
            return new ArrayList<Object[]>();
        } else {
            return listedResult;
        }
    }
}
