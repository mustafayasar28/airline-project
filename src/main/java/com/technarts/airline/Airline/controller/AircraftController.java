package com.technarts.airline.Airline.controller;

import com.technarts.airline.Airline.model.dto.aircraft.AircraftDTO;
import com.technarts.airline.Airline.model.dto.aircraft.CreateAircraftDTO;
import com.technarts.airline.Airline.model.dto.aircraft.UpdateAircraftDTO;
import com.technarts.airline.Airline.service.aircraft.AircraftService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/aircraft")
public class AircraftController {
    private final AircraftService aircraftService;

    public AircraftController(AircraftService aircraftService) {
        this.aircraftService = aircraftService;
    }

    @GetMapping
    public List<AircraftDTO> retrieveAllAircrafts() {
        return aircraftService.retrieveAllAircrafts();
    }

    @GetMapping("/{aircraftId}")
    public AircraftDTO retrieveAircraftById(@PathVariable UUID aircraftId) {
        return aircraftService.retrieveAircraft(aircraftId);
    }

    @PostMapping
    ResponseEntity<AircraftDTO> createAircraft(@RequestBody @Valid CreateAircraftDTO createAircraftDTO) {
        return new ResponseEntity<>(aircraftService.createAircraft(createAircraftDTO), HttpStatus.CREATED);
    }

    @PatchMapping("/{aircraftId}")
    public ResponseEntity<AircraftDTO> updateAircraft(@PathVariable UUID aircraftId,
                                                      @RequestBody UpdateAircraftDTO updateAircraftDTO) {
        return aircraftService.updateAircraft(aircraftId, updateAircraftDTO);
    }

    @DeleteMapping("/{aircraftId}")
    public ResponseEntity deleteAircraft(@PathVariable UUID aircraftId) {
        aircraftService.deleteAircraft(aircraftId);
        return ResponseEntity.noContent().build();
    }
}
