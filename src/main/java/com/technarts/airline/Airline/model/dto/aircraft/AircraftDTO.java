package com.technarts.airline.Airline.model.dto.aircraft;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AircraftDTO {
    private UUID id;
    private String manufacturer_serial_number;
    private String type;
    private String model;
    private Integer number_of_engines;

}
