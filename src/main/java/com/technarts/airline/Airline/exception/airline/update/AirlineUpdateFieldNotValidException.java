package com.technarts.airline.Airline.exception.airline.update;

import java.util.List;

public class AirlineUpdateFieldNotValidException extends RuntimeException {
    private final List<String> errors;

    public AirlineUpdateFieldNotValidException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
