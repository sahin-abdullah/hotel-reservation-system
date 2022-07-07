package com.vacationnow.entity.guest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min = 2, max = 20)
    @NotNull(message = "required")
    @Column(nullable = false)
    private String firstName;

    @Size(min = 2, max = 20)
    @NotNull(message = "required")
    @Column(nullable = false)
    private String lastName;

    private boolean child;

    public String getFormattedFullName() {
        return firstName.substring(0, 1).toUpperCase() +
                firstName.substring(1) + " " +
                lastName.substring(0, 1).toUpperCase() +
                lastName.substring(1);
    }

    public Guest(String firstName, String lastName, boolean child) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.child = child;
    }
}
