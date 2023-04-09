package com.technarts.airline.Airline.exception.airline;

public class AirlineNameUniqueException extends RuntimeException {
    public AirlineNameUniqueException(String message) {
        super(message);
    }
}