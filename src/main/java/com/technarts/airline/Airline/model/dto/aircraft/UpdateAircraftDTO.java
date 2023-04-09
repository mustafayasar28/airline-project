package com.technarts.airline.Airline.model.dto.aircraft;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateAircraftDTO {
    private String manufacturer_serial_number;
    private String type;
    private String model;
    private Integer number_of_engines;
}
