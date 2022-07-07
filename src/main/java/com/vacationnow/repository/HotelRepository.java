package com.vacationnow.repository;

import com.vacationnow.entity.hotel.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface HotelRepository extends PagingAndSortingRepository<Hotel, Long> {

    @Query(value = "SELECT * FROM Hotel " +
            "ORDER BY ACOS(SIN(Hotel.latitude*:sf)*SIN(:lat*:sf) + COS(Hotel.latitude*:sf)*COS(:lat*:sf)*COS((Hotel.longitude-:lng)*:sf))", nativeQuery = true)
    Page<Hotel> findClosestHotel(@Param("lat") Double lat, @Param("lng") Double lng, @Param("sf") Double sf, Pageable paging);

    @Query(value = "SELECT COUNT(*) FROM Hotel", nativeQuery = true)
    Integer getCount();

    Page<Hotel> findAll(Pageable paging);
}
