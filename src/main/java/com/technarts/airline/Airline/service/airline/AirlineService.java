package com.technarts.airline.Airline.service.airline;

import com.technarts.airline.Airline.model.dto.aircraft.AircraftDTO;
import com.technarts.airline.Airline.model.dto.airline.AirlineDTO;
import com.technarts.airline.Airline.model.dto.airline.CreateAirlineDTO;
import com.technarts.airline.Airline.model.dto.airline.UpdateAirlineDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface AirlineService {
    AirlineDTO createAirline(CreateAirlineDTO createAirlineDTO);

    ResponseEntity<AirlineDTO> updateAirline(UUID id, UpdateAirlineDTO updateAirlineDTO);

    AirlineDTO retrieveAirline(UUID id);

    List<AirlineDTO> retrieveAllAirlines();

    List<AircraftDTO> retrieveAllAircraftsOfAirline(UUID airlineId);

    void deleteAirline(UUID airlineId);
}
