package com.vacationnow.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ReservationDTO implements Serializable {
    @NotEmpty(message = "Destination cannot be empty")
    private String destination;

    @NotEmpty(message = "Dates cannot be empty")
    private String dateRange;

    @Min(value = 1)
    private Integer adult;
    @Min(value = 0)
    private Integer kid;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateFrom;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateTo;

}
