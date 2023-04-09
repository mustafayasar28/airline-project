package com.technarts.airline.Airline.model.dto.airline;

import lombok.*;

import java.util.UUID;

/**
 * This dto is returned to the user after the creation, update, and
 * search of an airline
 *
 * @author Mustafa Yasar
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AirlineDTO {
    private UUID id;
    private String name;
    private String callsign;
    private Integer founded_year;
    private String base_airport;
}
