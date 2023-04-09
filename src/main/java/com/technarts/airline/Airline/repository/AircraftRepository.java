package com.technarts.airline.Airline.repository;

import com.technarts.airline.Airline.model.entity.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AircraftRepository extends JpaRepository<Aircraft, UUID> {
    List<Aircraft> findAircraftsByAirlineId(UUID airlineId);
}