package com.technarts.airline.Airline.exception;

import com.technarts.airline.Airline.exception.aircraft.update.AircraftUpdateFieldNotValidException;
import com.technarts.airline.Airline.exception.airline.AirlineNameUniqueException;
import com.technarts.airline.Airline.exception.airline.AirlineNotFoundException;
import com.technarts.airline.Airline.exception.airline.update.AirlineUpdateFieldNotValidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AirlineNotFoundException.class)
    public ResponseEntity<Object> handleAirlineNotFoundException(AirlineNotFoundException exception) {
        Map<String, Object> errors = prepareErrorMap();
        errors.put("message", exception.getMessage());

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AirlineNameUniqueException.class)
    public ResponseEntity<Object> handleAirlineNameUniqueException(AirlineNameUniqueException ex) {
        // You can create a custom error response class if needed.
        // For simplicity, we'll create a HashMap to store the error message.
        Map<String, Object> errors = prepareErrorMap();

        errors.put("message", ex.getMessage());

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AirlineUpdateFieldNotValidException.class)
    public ResponseEntity<Object> handleAirlineUpdateFieldNotValidException(
            AirlineUpdateFieldNotValidException exception
    ) {
        Map<String, Object> errors = prepareErrorMap();
        errors.put("message", exception.getMessage());
        errors.put("errors", exception.getErrors());


        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AircraftUpdateFieldNotValidException.class)
    public ResponseEntity<Object> handleAircraftUpdateFieldNotValidException(
            AircraftUpdateFieldNotValidException exception
    ) {
        Map<String, Object> errors = prepareErrorMap();
        errors.put("message", exception.getMessage());
        errors.put("errors", exception.getErrors());


        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = prepareErrorMap();

        Map<String, String> validationErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            validationErrors.put(error.getField(), error.getDefaultMessage());
        }

        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            validationErrors.put(error.getObjectName(), error.getDefaultMessage());
        }

        errors.put("errors", validationErrors);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleOtherExceptions(RuntimeException e) {
        Map<String, Object> errors = prepareErrorMap();
        errors.put("message", e.getMessage());

        return new ResponseEntity<>(errors, HttpStatus.SERVICE_UNAVAILABLE);
    }

    private static Map<String, Object> prepareErrorMap() {
        Map<String, Object> errors = new HashMap<>();
        errors.put("status", HttpStatus.BAD_REQUEST.value());
        errors.put("timestamp", LocalDateTime.now());
        return errors;
    }
}