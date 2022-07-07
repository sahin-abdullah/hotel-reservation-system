package com.vacationnow.controller;

import com.vacationnow.dto.ReservationDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

@Controller
public class MainController {

    @GetMapping(value={"/", "/home", "/main"})
    public String homePage(Model model) {
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setDateFrom(LocalDate.now());
        reservationDTO.setDateTo(LocalDate.now().plusDays(1));
        model.addAttribute("reservationDTO", new ReservationDTO());
        return "main";
    }

    @GetMapping("/signin")
    public String login(HttpServletRequest request, Model model) {
        String referrer = request.getHeader("Referer");
        request.getSession().setAttribute("url_prior_login", referrer);
        return "signin";
    }
}
