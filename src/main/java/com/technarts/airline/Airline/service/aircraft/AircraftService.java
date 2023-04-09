package com.technarts.airline.Airline.service.aircraft;

import com.technarts.airline.Airline.model.dto.aircraft.AircraftDTO;
import com.technarts.airline.Airline.model.dto.aircraft.CreateAircraftDTO;
import com.technarts.airline.Airline.model.dto.aircraft.UpdateAircraftDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface AircraftService {
    AircraftDTO createAircraft(CreateAircraftDTO createAircraftDTO);

    ResponseEntity<AircraftDTO> updateAircraft(UUID id, UpdateAircraftDTO updateAircraftDTO);

    AircraftDTO retrieveAircraft(UUID id);

    List<AircraftDTO> retrieveAllAircrafts();

    void deleteAircraft(UUID aircraftId);
}
