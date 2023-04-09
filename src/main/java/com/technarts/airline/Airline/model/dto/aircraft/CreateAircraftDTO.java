package com.technarts.airline.Airline.model.dto.aircraft;

import lombok.*;

import javax.validation.constraints.*;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAircraftDTO {
    @NotNull
    private UUID operator_airline;

    @NotBlank(message = "Manufacturer serial number of an aircraft can not be blank")
    @Size(min = 2, max = 50, message = "Manufacturer serial number must be between 2 and 50 characters.")
    private String manufacturer_serial_number;

    @NotBlank(message = "Type of an airline can not be blank")
    @Size(min = 2, max = 50, message = "Type must be between 2 and 50 characters.")
    private String type;

    @NotBlank(message = "Model of an airline can not be blank")
    @Size(min = 2, max = 50, message = "Model must be between 2 and 50 characters.")
    private String model;

    @Max(value = 20, message = "The aircraft can have at most 20 engines")
    @Min(value = 1, message = "The aircraft must have at least 1 engine")
    private Integer number_of_engines;
}
