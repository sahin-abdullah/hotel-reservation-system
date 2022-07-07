package com.vacationnow.service;

import com.vacationnow.dto.UserDTO;
import com.vacationnow.entity.guest.Role;
import com.vacationnow.entity.guest.User;
import com.vacationnow.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public void register(UserDTO userDTO) throws Exception {
        if(checkIfUserExists(userDTO.getEmail())) {
            throw new Exception("User already exists");
        }
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        if (userDTO.getBusinessProfile() != null) {
            user.setRoles(Arrays.asList(new Role("BUSINESS"), new Role("USER")));
        } else {
            user.setRoles(List.of(new Role("USER")));
        }
        encodePassword(user, userDTO);
        userRepository.save(user);
    }

    public boolean checkIfUserExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public void encodePassword(User user, UserDTO userDTO) {
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
    }

}
