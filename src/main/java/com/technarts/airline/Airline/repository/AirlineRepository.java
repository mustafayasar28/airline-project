package com.technarts.airline.Airline.repository;

import com.technarts.airline.Airline.model.entity.Airline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AirlineRepository extends JpaRepository<Airline, UUID> {
}
