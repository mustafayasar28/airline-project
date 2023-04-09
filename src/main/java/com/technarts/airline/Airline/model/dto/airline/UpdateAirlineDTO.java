package com.technarts.airline.Airline.model.dto.airline;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateAirlineDTO {
    private String name;
    private String callsign;
    private Integer founded_year;
    private String base_airport;
}