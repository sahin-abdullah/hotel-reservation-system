package com.vacationnow.repository;

import com.vacationnow.entity.hotel.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query(value = "SELECT room.* " +
            "FROM room " +
            "LEFT JOIN reservation_room rr on rr.room_id = room.id " +
            "LEFT JOIN reservation rsvr on rr.reservation_id = rsvr.id " +
            "WHERE room.hotel_id = :hotel_id " +
            "AND " +
            "(:GuestCheckInDate <= check_out_date AND :GuestCheckOutDate >= check_in_date)"
            , nativeQuery = true)
    List<Room> findReservedRoom(@Param("hotel_id") Long hotelId,
                                @Param("GuestCheckInDate") LocalDate checkIn,
                                @Param("GuestCheckOutDate") LocalDate checkOut);

    @Query(value = "SELECT * FROM room " +
            "WHERE hotel_id = :hotel_id", nativeQuery = true)
    List<Room> findRoomByHotelId(@Param("hotel_id") Long hotelId);


    @Query(value = "SELECT room.* FROM room " +
            "LEFT JOIN reservation_room rr on room.id = rr.room_id " +
            "WHERE hotel_id = :hotel_id " +
            "AND " +
            "room_type = :roomType " +
            "AND " +
            "id NOT IN ( " +
            "    SELECT room_id FROM reservation_room " +
            "        ) " +
            "LIMIT :roomNum", nativeQuery = true)
    List<Object[]> willBeReserved(@Param("hotel_id") Long hotelId, @Param("roomType") String roomType, @Param("roomNum") Integer roomNum);
}

