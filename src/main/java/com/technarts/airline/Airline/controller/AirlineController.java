package com.technarts.airline.Airline.controller;

import com.technarts.airline.Airline.model.dto.aircraft.AircraftDTO;
import com.technarts.airline.Airline.model.dto.airline.AirlineDTO;
import com.technarts.airline.Airline.model.dto.airline.CreateAirlineDTO;
import com.technarts.airline.Airline.model.dto.airline.UpdateAirlineDTO;
import com.technarts.airline.Airline.service.airline.AirlineService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/airline")
public class AirlineController {

    private final AirlineService airlineService;

    public AirlineController(AirlineService airlineService) {
        this.airlineService = airlineService;
    }


    @GetMapping
    public List<AirlineDTO> retrieveAllAirlines() {
        return airlineService.retrieveAllAirlines();
    }

    @GetMapping("/{airlineId}")
    public AirlineDTO retrieveAirlineById(@PathVariable UUID airlineId) {
        return airlineService.retrieveAirline(airlineId);
    }

    @GetMapping("/{airlineId}/aircrafts")
    public List<AircraftDTO> retrieveAllAircraftsOfAirline(
            @PathVariable UUID airlineId
    ) {
        return airlineService.retrieveAllAircraftsOfAirline(airlineId);
    }

    @PostMapping
    public ResponseEntity<AirlineDTO> createAirline(@RequestBody @Valid CreateAirlineDTO createAirlineDTO) {
        return new ResponseEntity<>(airlineService.createAirline(createAirlineDTO), HttpStatus.CREATED);
    }

    @PatchMapping("/{airlineId}")
    public ResponseEntity<AirlineDTO> updateAirline(@PathVariable UUID airlineId,
                                    @RequestBody UpdateAirlineDTO updateAirlineDTO) {
        return airlineService.updateAirline(airlineId, updateAirlineDTO);
    }

    @DeleteMapping("/{airlineId}")
    public ResponseEntity deleteAirline(@PathVariable UUID airlineId) {
        airlineService.deleteAirline(airlineId);
        return ResponseEntity.noContent().build();
    }
}
