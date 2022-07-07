package com.vacationnow.controller;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.vacationnow.dto.*;
import com.vacationnow.entity.guest.Guest;
import com.vacationnow.entity.hotel.Hotel;
import com.vacationnow.entity.hotel.Room;
import com.vacationnow.entity.hotel.RoomType;
import com.vacationnow.entity.reservation.Reservation;
import com.vacationnow.entity.reservation.ReservationDates;
import com.vacationnow.service.CustomUserDetails;
import com.vacationnow.service.HotelServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@SessionAttributes({"reservationDTO", "roomDTO"})
public class HotelController {

    @Autowired
    private HotelServiceImpl hotelService;


    @GetMapping(value = "/search")
    public String getAllHotels(
            @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            Model model
    ) {
        List<Hotel> queryList = hotelService.getAllHotels(pageNo, pageSize, sortBy);

        List<HotelDTO> hotelList = new ArrayList<>();

        for (Hotel hotel : queryList) {
            hotelList.add(new HotelDTO(hotel));
        }

        model.addAttribute("hotelDTO", hotelList);
        return "/search";
    }

    @GetMapping("/search/hotels")
    public String getAvailableHotels(
            @RequestParam(name = "destination", defaultValue = "Boston") String destination,
            @RequestParam(name = "dateRange") String dateRange,
            @RequestParam(name = "adult", defaultValue = "1") Integer adult,
            @RequestParam(name = "kid", defaultValue = "0") Integer kid,
            @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "2") Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            Model model, HttpSession session
    ) throws IOException, InterruptedException, ApiException {

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setAdult(adult);
        reservationDTO.setDestination(destination);
        reservationDTO.setKid(kid);
        reservationDTO.setDateRange(dateRange);
        verifyDates(reservationDTO);
        model.addAttribute("reservationDTO", reservationDTO);

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("API_KEY")
                .build();
        GeocodingResult[] results =  GeocodingApi.geocode(context, reservationDTO.getDestination()).await();
        context.shutdown();
        Double latitude = results[0].geometry.location.lat;
        Double longitude = results[0].geometry.location.lng;
        List<Hotel> queryList = hotelService.getClosestHotels(latitude, longitude, pageNo, pageSize);

        Integer count = hotelService.getCountAllHotels();
        List<HotelDTO> hotelList = new ArrayList<>();

        for (Hotel hotel : queryList) {
            List<Room> allRooms = hotelService.getAllRooms(hotel.getId());
            List<Room> reservedRooms = hotelService.getReservedRooms(hotel.getId(), reservationDTO.getDateFrom(), reservationDTO.getDateTo());
            allRooms.removeAll(reservedRooms);
            Set<RoomType> roomTypes = getRoomTypes(allRooms);
            hotelList.add(new HotelDTO(hotel, allRooms, roomTypes, latitude, longitude));
        }
        model.addAttribute("paging", (count+pageSize-1)/pageSize - 1);
        model.addAttribute("hotelDTO", hotelList);
        return "/search";
    }

    @GetMapping("/hotels/{id}")
    public String getHotelPage(@PathVariable("id") Long hotelId, Model model,
                               @ModelAttribute ReservationDTO reservationDTO) {

        List<Room> allRooms = hotelService.getAllRooms(hotelId);
        List<Room> reservedRooms = hotelService.getReservedRooms(hotelId, reservationDTO.getDateFrom(), reservationDTO.getDateTo());
        allRooms.removeAll(reservedRooms);
        RoomDTO roomDTO = getRoomSummary(allRooms);
        model.addAttribute("roomDTO", roomDTO);
        Hotel hotel =  hotelService.getHotelById(hotelId);
        HotelDTO hotelDTO = new HotelDTO(hotel);
        model.addAttribute("hotelVarName", hotelDTO.getVarName());
        model.addAttribute("hotelImageURL", hotelDTO.getImageURI().split("_")[0]);
        model.addAttribute("hotel", hotel);
        model.addAttribute("hotelAddress", hotelDTO.getAddress());
        return "hotel";
    }

    @PostMapping("/reservation/hotels/{id}")
    public String getReservation(@PathVariable("id") Long hotelId, RoomDTO roomDTO,
                                 BindingResult bindingResult,
                                 Model model,
                                 @ModelAttribute ReservationDTO reservationDTO,
                                 RedirectAttributes rm) {

        int totalGuest = reservationDTO.getAdult() + reservationDTO.getKid();
        int capacity = 0;
        Hotel hotel = hotelService.getHotelById(hotelId);
        int duration = Period.between(reservationDTO.getDateFrom(), reservationDTO.getDateTo()).getDays();
        BigDecimal totalPrice = new BigDecimal(0);
        HashMap<String, Integer> maxCapacity = new HashMap<>();
        HashMap<String, BigDecimal> price = new HashMap<>();
        for (Room room : hotelService.getHotelById(hotelId).getRooms()) {
            maxCapacity.put(room.getRoomType().name(), room.getBeds());
            price.put(room.getRoomType().name(), room.getCostPerNight());
        }
        for (Map.Entry<String, Integer> entry : roomDTO.getDropDown().entrySet()) {
            capacity += maxCapacity.get(entry.getKey()) * entry.getValue();
            totalPrice = totalPrice.add(price.get(entry.getKey()).multiply(BigDecimal.valueOf(duration)).multiply(BigDecimal.valueOf(entry.getValue())));
        }
        if (totalGuest > capacity) {
            rm.addFlashAttribute("errorMessage","Total guest must fit in selected room(s)");
            return "redirect:/hotels/" + hotelId ;
        }

        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("hotelSummary", hotel);
        model.addAttribute("reservationDTO", reservationDTO);

        GuestDTO guestDTO = new GuestDTO();
        for (int i = 0; i<reservationDTO.getAdult(); i++) {
            Guest guest = new Guest();
            guest.setFirstName("");
            guest.setLastName("");
            guest.setChild(false);
            guestDTO.getGuestList().add(guest);
        }
        for (int i = 0; i<reservationDTO.getKid(); i++) {
            Guest guest = new Guest();
            guest.setFirstName("");
            guest.setLastName("");
            guest.setChild(true);
            guestDTO.getGuestList().add(guest);
        }
        model.addAttribute("guestDTO", guestDTO);

        return "reservation";
    }

    @GetMapping("/my_reservation")
    public String myReservationPage(Model model) {
        MyReservationList myReservationList = getReservationById();
        model.addAttribute("myReservationList", myReservationList);
        return "/my_reservation";
    }

    @GetMapping("/hotels/{id}/make_reservation")
    public String makeReservation(GuestDTO guestDTO, RoomDTO roomDTO,
                                  @PathVariable("id") Long hotelId,
                                  @ModelAttribute ReservationDTO reservationDTO, Model model) {
        Set<Guest> guests = new HashSet<>(guestDTO.getGuestList());
        Set<Room> rooms = new HashSet<>();
        for (Map.Entry<String, Integer> entry : roomDTO.getDropDown().entrySet()) {
            rooms.addAll(hotelService.reserveRooms(hotelId, entry.getKey(), entry.getValue()));
        }
        ReservationDates reservationDates = new ReservationDates(reservationDTO.getDateFrom(), reservationDTO.getDateTo());
        Reservation reservation = new Reservation(rooms, guests, reservationDates, LocalDateTime.now());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        reservation.setUserId(user.getUser().getId());
        hotelService.saveReservation(reservation);

        MyReservationList myReservationList = getReservationById();
        model.addAttribute("myReservationList", myReservationList);

        return "/my_reservation";
    }

    @RequestMapping("/cancel_reservation/{id}")
    public String removeReservation(@PathVariable("id") Long reservationId, Model model) {
        hotelService.removeReservation(reservationId);
        MyReservationList myReservationList = getReservationById();
        model.addAttribute("myReservationList", myReservationList);
        return "/my_reservation";
    }

    public MyReservationList getReservationById() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        Long userId = user.getUser().getId();
        List<Object[]> myTempList = hotelService.findReservationById(userId);
        MyReservationList myReservationList = new MyReservationList();
        for (Object[] obj : myTempList) {
            BigInteger id = (BigInteger) obj[0];
            Date checkIn = (Date) obj[3];
            Date checkOut = (Date) obj[4];
            Timestamp creationDate = (Timestamp) obj[5]; //
            MyReservationDTO myReservationDTO = new MyReservationDTO(
                    id.longValue(), (String) obj[1], (Integer) obj[2],
                    checkIn.toLocalDate(), checkOut.toLocalDate(), creationDate.toLocalDateTime().toLocalDate());
            myReservationList.getMyReservationDTOList().add(myReservationDTO);
        }
        return myReservationList;
    }

    public void verifyDates(ReservationDTO reservationDTO) {
        String dateRange = reservationDTO.getDateRange();
        String[] dateArray = dateRange.split("-");
        LocalDate checkInDate = LocalDate.parse(dateArray[0].trim(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        LocalDate checkOutDate = LocalDate.parse(dateArray[1].trim(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));

        if(reservationDTO.getDateFrom() == null || reservationDTO.getDateTo() == null) {
            reservationDTO.setDateFrom(checkInDate);
            reservationDTO.setDateTo(checkOutDate);
        } else if (!checkInDate.isEqual(reservationDTO.getDateFrom()) || !checkOutDate.isEqual(reservationDTO.getDateTo())) {
            reservationDTO.setDateFrom(checkInDate);
            reservationDTO.setDateTo(checkOutDate);
        }
    }

    public Set<RoomType> getRoomTypes(List<Room> rooms) {
        Set<RoomType> roomTypes = new HashSet<>();
        for(Room room: rooms) {
            roomTypes.add(room.getRoomType());
        }
        return roomTypes;
    }

    public RoomDTO getRoomSummary(List<Room> allRooms) {
        RoomDTO roomDTO = new RoomDTO();
        for (Room room : allRooms) {
            roomDTO.getAvailableRoom().put(room.getRoomType().name(), roomDTO.getAvailableRoom().getOrDefault(room.getRoomType().name(), 0)+1);
            roomDTO.getPrice().put(room.getRoomType().name(), room.getCostPerNight());
            roomDTO.getBeds().put(room.getRoomType().name(), room.getBeds());
        }
        return roomDTO;
    }
}