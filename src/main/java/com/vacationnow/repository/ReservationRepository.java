package com.vacationnow.repository;

import com.vacationnow.entity.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Override
    void deleteById(Long aLong);

    @Query(value= "SELECT DISTINCT reservation.id AS id, h.name as hotelName, h.stars AS hotelStar, " +
            "reservation.check_in_date AS checkInDate, " +
            "reservation.check_out_date AS checkOutDate, " +
            "reservation.created_time AS creationDate FROM reservation " +
            "JOIN reservation_room rr ON rr.reservation_id = reservation.id " +
            "JOIN room r on rr.room_id = r.id " +
            "JOIN hotel h on r.hotel_id = h.id " +
            "WHERE reservation.user_id = :userId", nativeQuery = true)
    List<Object[]> findReservationById(@Param("userId") Long userId);

}
