package com.vacationnow.entity.location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Postcode {
    @Column(nullable = false)
    @Pattern(regexp = "[0-9]{5}", message = "Postcode must be 5 digits")
    @NotNull(message = "required")
    private String value;
}
