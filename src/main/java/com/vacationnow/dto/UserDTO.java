package com.vacationnow.dto;

import com.vacationnow.security.FieldMatch;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;


@Getter
@Setter
@FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match")
public class UserDTO implements Serializable {
    @NotEmpty(message = "First name cannot be empty")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    private String lastName;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Please provide a valid email address")
    private String email;

    @Size(min = 8, max = 25, message = "Size must be between 8 and 25")
    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @Size(min = 8, max = 25, message = "Size must be between 8 and 25")
    @NotEmpty(message = "Password cannot be empty")
    private String confirmPassword;

    private Boolean businessProfile;

    public UserDTO() {
    }

    public UserDTO (String firstName, String lastName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }
}
