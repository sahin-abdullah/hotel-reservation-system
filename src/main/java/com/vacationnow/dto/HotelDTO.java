package com.vacationnow.dto;

import com.vacationnow.entity.hotel.Hotel;
import com.vacationnow.entity.hotel.Room;
import com.vacationnow.entity.hotel.RoomType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.*;

@Getter
@Setter
public class HotelDTO {
    private Long id;
    private String varName;
    private String hotelName;
    private int star;
    private String address;

    private String imageURI;

    private Double distance;

    private List<Room> room = new ArrayList<>();

    private Set<RoomType> roomSet = new HashSet<>();

    private Map<String, String> nameMap;

    private BigDecimal minPrice;

    {
        nameMap = new HashMap<>();
        nameMap.put("New England Suite Hotel", "newEnglandHotel");
        nameMap.put("The Grand Serra Hotel", "serraHotel");
        nameMap.put("Arsenal Hotel & Casino", "arsenalHotel");
        nameMap.put("Cambridge Hotel", "cambridgeHotel");
        nameMap.put("Harvard Suite Hotel", "harvardSuiteHotel");
        nameMap.put("Boston Central Hotel", "bostonCentralHotel");
    }

    public HotelDTO(Hotel hotel, List<Room> room, Set<RoomType> roomSet, Double lat, Double lng) {
        this.id = hotel.getId();
        this.hotelName = hotel.getName();
        this.star = hotel.getStars();
        String streetLine2 = hotel.getAddress().getStreetLine2() == null ? "" : hotel.getAddress().getStreetLine2();
        this.address = hotel.getAddress().getStreetLine1() +
                " " + streetLine2 +
                " " + hotel.getAddress().getCity() + ", " +
                " " + hotel.getAddress().getState() +
                " " + hotel.getAddress().getPostcode().getValue();
        this.varName = nameMap.get(hotelName);
        this.roomSet = roomSet;
        this.imageURI = hotel.getImageUriSet().get(0).getImageUri();
        this.room = room;
        this.minPrice = hotel.getRooms().stream().min(Comparator.comparing(Room::getCostPerNight)).orElseThrow().getCostPerNight();
        double R = 3958.8;
        double rlat1 = hotel.getAddress().getLatitude() * (Math.PI/180);
        double rlat2 = lat * (Math.PI/180);
        double diffLat = rlat2 - rlat1;
        double diffLon = (hotel.getAddress().getLongitude() - lng) * (Math.PI/180);
        double d = 2 * R * Math.asin(Math.sqrt(Math.sin(diffLat/2)*Math.sin(diffLat/2)+Math.cos(rlat1)*Math.cos(rlat2)*Math.sin(diffLon/2)*Math.sin(diffLon/2)));
        this.distance = Math.round(d * 100.0) / 100.0;
    }

    public HotelDTO(Hotel hotel) {
        this.hotelName = hotel.getName();
        this.star = hotel.getStars();
        String streetLine2 = hotel.getAddress().getStreetLine2() == null ? "" : hotel.getAddress().getStreetLine2();
        this.address = hotel.getAddress().getStreetLine1() +
                " " + streetLine2 +
                " " + hotel.getAddress().getCity() + ", " +
                " " + hotel.getAddress().getState() +
                " " + hotel.getAddress().getPostcode().getValue();
        this.varName = nameMap.get(hotelName);
        this.imageURI = hotel.getImageUriSet().get(0).getImageUri();
    }
}
