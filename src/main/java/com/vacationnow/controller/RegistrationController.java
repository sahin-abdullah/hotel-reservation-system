package com.vacationnow.controller;

import com.vacationnow.dto.UserDTO;
import com.vacationnow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "register";
    }

    @PostMapping("/register")
    public String userRegistration(@Valid UserDTO userDTO, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("userDTO", userDTO);
            return "register";
        }
        try
        {
            userService.register(userDTO);
        } catch (Exception e) {
            bindingResult.rejectValue("email", "userDTO.email", "An Account already exists for this email");
            model.addAttribute("userDTO", userDTO);
            return "register";
        }
        return "redirect:/";
    }

}
