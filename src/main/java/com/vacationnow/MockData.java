package com.vacationnow;

import com.vacationnow.entity.ImageUri;
import com.vacationnow.entity.guest.Guest;
import com.vacationnow.entity.guest.Role;
import com.vacationnow.entity.guest.User;
import com.vacationnow.entity.hotel.Hotel;
import com.vacationnow.entity.hotel.Room;
import com.vacationnow.entity.hotel.RoomType;
import com.vacationnow.entity.location.Address;
import com.vacationnow.entity.location.Postcode;
import com.vacationnow.entity.location.State;
import com.vacationnow.entity.reservation.Reservation;
import com.vacationnow.entity.reservation.ReservationDates;
import com.vacationnow.repository.HotelRepository;
import com.vacationnow.repository.ReservationRepository;
import com.vacationnow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Component
public class MockData {

    private HotelRepository hotelRepository;

    private UserRepository userRepository;

    private ReservationRepository reservationRepository;

    @Autowired
    private ApplicationContext applicationContext;

    public MockData(HotelRepository hotelRepository, ReservationRepository reservationRepository,
                    UserRepository userRepository) {
        this.hotelRepository = hotelRepository;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
    }

    @Bean
    public CommandLineRunner insertTestData() {
        return args -> {
            System.out.println("------CommandLineRunner, Inserting Sample Data------");
            createUser();
            createHotel1();
            createHotel2();
            createHotel3();
            createHotel4();
            createHotel5();
            createHotel6();
        };
    }

    private void createUser() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Role role = new Role("USER");
        List<Role> myRole = new ArrayList<>();
        myRole.add(role);
        User user = new User();
        user.setFirstName("Abdullah");
        user.setLastName("Sahin");
        user.setEmail("sahin.csci@gmail.com");
        user.setPassword(encoder.encode("12345678"));
        user.setRoles(myRole);
//        user.setCreationDate(new Date());
        userRepository.save(user);
    }

    private void createHotel1() throws IOException {
        LocalTime earliestCheckInTime = LocalTime.of(12, 0);
        LocalTime latestCheckInTime = LocalTime.of(18, 0);
        LocalTime checkOutTime = LocalTime.of(10, 0);
        BigDecimal lateCheckoutFee = BigDecimal.valueOf(99.99);

        Address address = new Address("363 Great Rd", null, "Bedford", State.MA, new Postcode("01730"), 42.48405630716371, -71.26057313815433);

        Hotel newEnglandSuiteHotel = new Hotel("New England Suite Hotel", address, 4, "info@newenglandhotel.com",
                earliestCheckInTime,
                latestCheckInTime,
                checkOutTime,
                lateCheckoutFee);

        Room room1 = new Room("NE1001", RoomType.Economy, 1, BigDecimal.valueOf(49.99));
        Room room2 = new Room("NE1002", RoomType.Economy, 1, BigDecimal.valueOf(49.99));
        Room room3 = new Room("NE1003", RoomType.Business, 2, BigDecimal.valueOf(120.00));
        Room room4 = new Room("NE2001", RoomType.Business, 2, BigDecimal.valueOf(120.00));
        Room room5 = new Room("NE2002", RoomType.Suite, 4, BigDecimal.valueOf(599.99));
        Room room6 = new Room("NE2003", RoomType.Suite, 4, BigDecimal.valueOf(599.99));

        Room[] rooms = {room1, room2, room3, room4, room5, room6};

        for (Room room : rooms) {
            newEnglandSuiteHotel.addRoom(room);
        }

        Resource[] resources = applicationContext.getResources("classpath:static/images/hotels/newEnglandHotel/*.jpg");

        for (Resource file : resources) {
            ImageUri image = new ImageUri(file.getFilename());
            newEnglandSuiteHotel.addImage(image);
        }

        hotelRepository.save(newEnglandSuiteHotel);
    }

    private void createHotel2() throws IOException {
        LocalTime earliestCheckInTime = LocalTime.of(13, 0);
        LocalTime latestCheckInTime = LocalTime.of(20, 0);
        LocalTime checkOutTime = LocalTime.of(11, 0);
        BigDecimal lateCheckoutFee = BigDecimal.valueOf(15.99);

        Address address = new Address("4 Kingsley Ter", null, "Lynn", State.MA, new Postcode("01902"), 42.46925231251568, -70.9366565766149);

        Hotel serraHotel = new Hotel("The Grand Serra Hotel", address, 5, "info@thegrandserrahotel.com",
                earliestCheckInTime,
                latestCheckInTime,
                checkOutTime,
                lateCheckoutFee);

        Room room1 = new Room("S1001", RoomType.Business, 2, BigDecimal.valueOf(105.45));
        Room room2 = new Room("S1002", RoomType.Business, 2, BigDecimal.valueOf(105.45));
        Room room3 = new Room("S1003", RoomType.Business, 2, BigDecimal.valueOf(105.45));
        Room room4 = new Room("S2001", RoomType.Deluxe, 3, BigDecimal.valueOf(215.99));
        Room room5 = new Room("S2002", RoomType.Deluxe, 3, BigDecimal.valueOf(215.99));
        Room room6 = new Room("S2003", RoomType.Deluxe, 3, BigDecimal.valueOf(215.99));
        Room room7 = new Room("S3001", RoomType.Suite, 4, BigDecimal.valueOf(559.99));
        Room room8 = new Room("S3002", RoomType.Suite, 4, BigDecimal.valueOf(559.99));

        Room[] rooms = {room1, room2, room3, room4, room5, room6, room7, room8};

        for (Room room : rooms) {
            serraHotel.addRoom(room);
        }

        Resource[] resources = applicationContext.getResources("classpath:static/images/hotels/serraHotel/*.jpg");

        for (Resource file : resources) {
            ImageUri image = new ImageUri(file.getFilename());
            serraHotel.addImage(image);
        }

        hotelRepository.save(serraHotel);

        Guest guest1 = new Guest("Abdullah", "Sahin", false);
        Guest guest2 = new Guest("Sukriye", "Sahin", false);
        Set<Guest> guests= new HashSet<>();
        guests.add(guest1);
        guests.add(guest2);

        guest1 = new Guest("Abdullah2", "Sahin", false);
        guest2 = new Guest("Sukriye2", "Sahin", false);
        Set<Guest> guests2= new HashSet<>();
        guests2.add(guest1);
        guests2.add(guest2);

        Set<Room> roomList = new HashSet<>();
        roomList.add(room1);
        roomList.add(room2);

        Set<Room> roomList2 = new HashSet<>();
        roomList2.add(room3);
        roomList2.add(room4);

        ReservationDates reservationDates = new ReservationDates(LocalDate.now(), LocalDate.now().plusDays(1));
        ReservationDates reservationDates2 = new ReservationDates(LocalDate.parse("2011-01-01"), LocalDate.parse("2012-01-01"));

        Reservation reservation1 = new Reservation(roomList, guests, reservationDates, LocalDateTime.now());
        Reservation reservation2 = new Reservation(roomList, guests2, reservationDates2, LocalDateTime.now());
        reservation1.setUserId(Long.valueOf("1"));
        reservation2.setUserId(Long.valueOf("1"));
        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);
    }

    private void createHotel3() throws IOException {
        LocalTime earliestCheckInTime = LocalTime.of(13, 0);
        LocalTime latestCheckInTime = LocalTime.of(20, 0);
        LocalTime checkOutTime = LocalTime.of(11, 0);
        BigDecimal lateCheckoutFee = BigDecimal.valueOf(5.99);

        Address address = new Address("81 Wyman St", null, "Waltham", State.MA, new Postcode("02451"), 42.399860884557924, -71.25817821353496);

        Hotel arsenalHotel = new Hotel("Arsenal Hotel & Casino", address, 3, "info@arsenalhotel.com",
                earliestCheckInTime,
                latestCheckInTime,
                checkOutTime,
                lateCheckoutFee);

        Room room1 = new Room("WL1001", RoomType.Economy, 2, BigDecimal.valueOf(64.99));
        Room room2 = new Room("WL2001", RoomType.Economy, 2, BigDecimal.valueOf(64.99));
        Room room3 = new Room("WL2002", RoomType.Economy, 2, BigDecimal.valueOf(64.99));
        Room room4 = new Room("WL3002", RoomType.Business, 2, BigDecimal.valueOf(109.99));
        Room room5 = new Room("WL3003", RoomType.Business, 2, BigDecimal.valueOf(109.99));

        Room[] rooms = {room1, room2, room3, room4, room5};

        for (Room room : rooms) {
            arsenalHotel.addRoom(room);
        }

        Resource[] resources = applicationContext.getResources("classpath:static/images/hotels/arsenalHotel/*.jpg");

        for (Resource file : resources) {
            ImageUri image = new ImageUri(file.getFilename());
            arsenalHotel.addImage(image);
        }

        hotelRepository.save(arsenalHotel);
    }


    private void createHotel4() throws IOException {
        LocalTime earliestCheckInTime = LocalTime.of(13, 0);
        LocalTime latestCheckInTime = LocalTime.of(20, 0);
        LocalTime checkOutTime = LocalTime.of(11, 0);
        BigDecimal lateCheckoutFee = BigDecimal.valueOf(25.99);

        Address address = new Address("808 Memorial Dr", null, "Cambridge", State.MA, new Postcode("02139"), 42.360732737116024, -71.11538377386957);

        Hotel cambridgeHotel = new Hotel("Cambridge Hotel", address, 4, "info@cambridgehotel.com",
                earliestCheckInTime,
                latestCheckInTime,
                checkOutTime,
                lateCheckoutFee);

        Room room1 = new Room("CM1001", RoomType.Economy, 2, BigDecimal.valueOf(64.99));
        Room room2 = new Room("CM2001", RoomType.Economy, 2, BigDecimal.valueOf(64.99));
        Room room3 = new Room("CM2002", RoomType.Economy, 2, BigDecimal.valueOf(64.99));
        Room room4 = new Room("CM3001", RoomType.Economy, 2, BigDecimal.valueOf(64.99));
        Room room5 = new Room("CM3002", RoomType.Business, 2, BigDecimal.valueOf(159.99));
        Room room6 = new Room("CM3003", RoomType.Business, 2, BigDecimal.valueOf(159.99));
        Room room7 = new Room("CM3004", RoomType.Deluxe, 3, BigDecimal.valueOf(299.99));
        Room room8 = new Room("CM3005", RoomType.Deluxe, 3, BigDecimal.valueOf(299.99));

        Room[] rooms = {room1, room2, room3, room4, room5, room6, room7, room8};

        for (Room room : rooms) {
            cambridgeHotel.addRoom(room);
        }

        Resource[] resources = applicationContext.getResources("classpath:static/images/hotels/cambridgeHotel/*.jpg");

        for (Resource file : resources) {
            ImageUri image = new ImageUri(file.getFilename());
            cambridgeHotel.addImage(image);
        }

        hotelRepository.save(cambridgeHotel);
    }


    private void createHotel5() throws IOException {
        LocalTime earliestCheckInTime = LocalTime.of(13, 0);
        LocalTime latestCheckInTime = LocalTime.of(20, 0);
        LocalTime checkOutTime = LocalTime.of(11, 0);
        BigDecimal lateCheckoutFee = BigDecimal.valueOf(25.99);

        Address address = new Address("61 Creighton St", null, "Cambridge", State.MA, new Postcode("02140"), 42.38944747937227, -71.12378386209114);

        Hotel harvardSuiteHotel = new Hotel("Harvard Suite Hotel", address, 5, "info@harvardhotel.com",
                earliestCheckInTime,
                latestCheckInTime,
                checkOutTime,
                lateCheckoutFee);

        Room room1 = new Room("HV1001", RoomType.Business, 2, BigDecimal.valueOf(189.99));
        Room room2 = new Room("HV2001", RoomType.Business, 2, BigDecimal.valueOf(189.99));
        Room room3 = new Room("HV2002", RoomType.Deluxe, 2, BigDecimal.valueOf(289.99));
        Room room4 = new Room("HV3001", RoomType.Deluxe, 2, BigDecimal.valueOf(289.99));
        Room room5 = new Room("HV3002", RoomType.Suite, 3, BigDecimal.valueOf(499.99));
        Room room6 = new Room("HV3003", RoomType.Suite, 3, BigDecimal.valueOf(499.99));

        Room[] rooms = {room1, room2, room3, room4, room5, room6};

        for (Room room : rooms) {
            harvardSuiteHotel.addRoom(room);
        }

        Resource[] resources = applicationContext.getResources("classpath:static/images/hotels/harvardSuiteHotel/*.jpg");

        for (Resource file : resources) {
            ImageUri image = new ImageUri(file.getFilename());
            harvardSuiteHotel.addImage(image);
        }

        hotelRepository.save(harvardSuiteHotel);
    }

    private void createHotel6() throws IOException {
        LocalTime earliestCheckInTime = LocalTime.of(13, 0);
        LocalTime latestCheckInTime = LocalTime.of(20, 0);
        LocalTime checkOutTime = LocalTime.of(11, 0);
        BigDecimal lateCheckoutFee = BigDecimal.valueOf(45.99);

        Address address = new Address("528 Massachusetts Ave", null, "Boston", State.MA, new Postcode("02118"), 42.33892380275931, -71.07941830271054);

        Hotel bostonCentralHotel = new Hotel("Boston Central Hotel", address, 4, "info@bostonhotel.com",
                earliestCheckInTime,
                latestCheckInTime,
                checkOutTime,
                lateCheckoutFee);

        Room room1 = new Room("BS1001", RoomType.Economy, 2, BigDecimal.valueOf(69.99));
        Room room2 = new Room("BS1002", RoomType.Economy, 2, BigDecimal.valueOf(69.99));
        Room room3 = new Room("BS2001", RoomType.Business, 2, BigDecimal.valueOf(129.99));
        Room room4 = new Room("BS2002", RoomType.Business, 2, BigDecimal.valueOf(129.99));
        Room room5 = new Room("BS3001", RoomType.Deluxe,3, BigDecimal.valueOf(299.99));
        Room room6 = new Room("BS3002", RoomType.Deluxe, 3, BigDecimal.valueOf(299.99));

        Room[] rooms = {room1, room2, room3, room4, room5, room6};

        for (Room room : rooms) {
            bostonCentralHotel.addRoom(room);
        }

        Resource[] resources = applicationContext.getResources("classpath:static/images/hotels/bostonCentralHotel/*.jpg");

        for (Resource file : resources) {
            ImageUri image = new ImageUri(file.getFilename());
            bostonCentralHotel.addImage(image);
        }

        hotelRepository.save(bostonCentralHotel);
    }

}
