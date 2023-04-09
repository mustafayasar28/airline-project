package com.technarts.airline.Airline.exception.aircraft.update;

import java.util.List;

public class AircraftUpdateFieldNotValidException extends RuntimeException {
    private final List<String> errors;

    public AircraftUpdateFieldNotValidException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
