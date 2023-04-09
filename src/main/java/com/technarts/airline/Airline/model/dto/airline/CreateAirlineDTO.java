package com.technarts.airline.Airline.model.dto.airline;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAirlineDTO {

    @NotBlank(message = "Name of an airline can not be blank")
    @Size(min = 2, max = 50, message = "Airline name must be between 2 and 50 characters.")
    private String name;

    @NotBlank(message = "Callsign of an airline can not be blank")
    @Size(min = 2, max = 50, message = "Callsign must be between 2 and 50 characters.")
    private String callsign;

    @Max(value = 2023, message = "Founded year of an airline can not exceed 2023")
    @Min(value = 1700, message = "Founded year of an airline can not be less than 1700")
    private Integer founded_year;

    @NotBlank(message = "Base airport of an airline can not be blank")
    @Size(min = 2, max = 50, message = "Base Airport Name must be between 2 and 50 characters.")
    private String base_airport;
}
