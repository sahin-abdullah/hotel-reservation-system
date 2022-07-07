package com.vacationnow.service;

import com.vacationnow.dto.UserDTO;
import com.vacationnow.entity.guest.User;

public interface UserService {
    void register(UserDTO userDTO) throws Exception;

    boolean checkIfUserExists(String email);

    void encodePassword(User user, UserDTO userDTO);

}
