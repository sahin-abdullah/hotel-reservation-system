package com.vacationnow.booking;


//import com.demo.domain.Hotel;
//import com.demo.domain.location.Address;
//import com.demo.domain.location.Postcode;
//import com.demo.domain.location.State;
//import com.demo.persistance.predicates.HotelPredicates;
import com.vacationnow.entity.hotel.Hotel;
import com.vacationnow.entity.location.Address;
import com.vacationnow.entity.location.Postcode;
import com.vacationnow.entity.location.State;
import com.vacationnow.repository.HotelRepository;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.data.domain.Page;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PersistenceTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private HotelRepository hotelRepository;

    private List<String> pageToHotelNames(Page<Hotel> page) {
        List<String> results = new ArrayList<>();
        page.map(Hotel::getName).forEach(results::add);
        return results;
    }

    @Test
    public void findAllLocation() {
        entityManager.persist(new Hotel("Hotel Royal",
                new Address("33 kent street", null, "Boston", State.MA, new Postcode("02478"), 0.0, 0.0),
                4, "royal@hotel.com"));

        entityManager.persist(new Hotel("Hotel Summer",
                new Address("133 kennedy avenue", null, "Austin", State.TX, new Postcode("78741"), 0.0, 0.0),
                4, "summer@hotel.com"));

        entityManager.persist(new Hotel("Hotel EastNight",
                new Address("54 east avenue", null, "Irvine", State.CA, new Postcode("92617"), 0.0, 0.0),
                4, "eastnight@hotel.com"));

        // empty matches all
        Page<Hotel> pageResult = hotelRepository.findAll(PageRequest.of(0, 20));

        assertThat(pageResult.getTotalElements(), Matchers.is(3L));
        assertThat(pageToHotelNames(pageResult),
                Matchers.containsInAnyOrder("Hotel Royal", "Hotel Summer", "Hotel EastNight"));
    }
}