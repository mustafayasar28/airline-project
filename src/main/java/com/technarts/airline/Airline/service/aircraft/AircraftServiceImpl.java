package com.technarts.airline.Airline.service.aircraft;

import com.technarts.airline.Airline.exception.aircraft.AircraftNotFoundException;
import com.technarts.airline.Airline.exception.aircraft.update.AircraftUpdateFieldNotValidException;
import com.technarts.airline.Airline.exception.airline.AirlineNotFoundException;
import com.technarts.airline.Airline.model.dto.aircraft.AircraftDTO;
import com.technarts.airline.Airline.model.dto.aircraft.CreateAircraftDTO;
import com.technarts.airline.Airline.model.dto.aircraft.UpdateAircraftDTO;
import com.technarts.airline.Airline.model.entity.Aircraft;
import com.technarts.airline.Airline.model.entity.Airline;
import com.technarts.airline.Airline.model.entity.User;
import com.technarts.airline.Airline.repository.AircraftRepository;
import com.technarts.airline.Airline.repository.AirlineRepository;
import com.technarts.airline.Airline.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AircraftServiceImpl implements AircraftService{
    private final AircraftRepository aircraftRepository;
    private final AirlineRepository airlineRepository;
    private final UserRepository userRepository;

    public AircraftServiceImpl(AircraftRepository aircraftRepository, AirlineRepository airlineRepository, UserRepository userRepository) {
        this.aircraftRepository = aircraftRepository;
        this.airlineRepository = airlineRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AircraftDTO createAircraft(CreateAircraftDTO createAircraftDTO) {
        Airline airline = airlineRepository.findById(createAircraftDTO.getOperator_airline())
                .orElseThrow(() -> new AirlineNotFoundException("Airline not found with ID: " + createAircraftDTO.getOperator_airline()));
        User currentUser = getCurrentUser();

        if (!airline.getCreator().equals(
                currentUser
        )) {
            throw new RuntimeException("Only the creator of the airline " +
                    "can create aircraft for the airline");
        }

        Aircraft aircraftEntity = Aircraft.builder()
                .number_of_engines(createAircraftDTO.getNumber_of_engines())
                .manufacturer_serial_number(createAircraftDTO.getManufacturer_serial_number())
                .model(createAircraftDTO.getModel())
                .type(createAircraftDTO.getType())
                .airline(airline)
                .creator(currentUser)
                .build();

        try {
            aircraftEntity = aircraftRepository.save(aircraftEntity);
            log.info("Aircraft has been created with id: " + aircraftEntity.getId());
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while saving the aircraft: " + e.getMessage());
        }

        return getAircraftDTO(aircraftEntity);
    }

    @Override
    public ResponseEntity<AircraftDTO> updateAircraft(UUID id, UpdateAircraftDTO updateAircraftDTO) {
        Aircraft aircraft = aircraftRepository.findById(id)
                .orElseThrow(() -> new AircraftNotFoundException(
                        "Aircraft not found with id: " + id
                ));

        User currentUser = getCurrentUser();

        if (!aircraft.getCreator().equals(
                currentUser
        )) {
            throw new RuntimeException("Only the creator of the aircraft " +
                    "can update the aircraft");
        }

        List<String> errors = updateAircraftFields(updateAircraftDTO, aircraft);

        if (!errors.isEmpty())
            throw new AircraftUpdateFieldNotValidException(
                    "Update of the aircraft failed", errors
            );

        try {
            aircraft = aircraftRepository.save(aircraft);
            log.info("Aircraft with id: " + aircraft.getId() + " has been updated");
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while updating the aircraft: " + e.getMessage());
        }

        return new ResponseEntity<>(getAircraftDTO(aircraft), HttpStatus.OK);
    }

    @Override
    public AircraftDTO retrieveAircraft(UUID id) {
        Aircraft aircraft = aircraftRepository.findById(id)
                .orElseThrow(() -> new AircraftNotFoundException(
                        "Aircraft not found with ID: " + id
                ));

        return getAircraftDTO(aircraft);
    }

    @Override
    public List<AircraftDTO> retrieveAllAircrafts() {
        List<Aircraft> aircraftList
                = aircraftRepository.findAll();

        return aircraftList.stream().map(AircraftServiceImpl::getAircraftDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteAircraft(UUID aircraftId) {
        Aircraft aircraft = aircraftRepository.findById(aircraftId)
                .orElseThrow(() -> new AircraftNotFoundException(
                        "Aircraft with id " + aircraftId + " not found"
                ));

        User currentUser = getCurrentUser();

        if (!aircraft.getCreator().equals(
                currentUser
        )) {
            throw new RuntimeException("Only the creator of the aircraft " +
                    "can delete the aircraft");
        }

        try {
            aircraftRepository.delete(aircraft);
            log.info("The aircraft with id " + aircraftId + " has been deleted");
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while deleting the aircraft with id: " +
                    aircraftId + " " + e.getMessage());
        }
    }

    public static AircraftDTO getAircraftDTO(Aircraft aircraft) {
        return AircraftDTO.builder()
                .id(aircraft.getId())
                .type(aircraft.getType())
                .model(aircraft.getModel())
                .manufacturer_serial_number(aircraft.getManufacturer_serial_number())
                .number_of_engines(aircraft.getNumber_of_engines())
                .build();
    }


    private List<String> updateAircraftFields(UpdateAircraftDTO updateAircraftDTO, Aircraft aircraft) {
        List<String> errors = new ArrayList<>();

        if (updateAircraftDTO.getType() != null) {
            if (updateAircraftDTO.getType().isBlank() ||
                updateAircraftDTO.getType().length() < 2 ||
                updateAircraftDTO.getType().length() > 50) {
                errors.add("The new type of an aircraft must be between 2 and 50 characters");
            } else {
                aircraft.setType(updateAircraftDTO.getType());
            }
        }

        if (updateAircraftDTO.getModel() != null) {
            if (updateAircraftDTO.getModel().isBlank() ||
                updateAircraftDTO.getModel().length() < 2 ||
                updateAircraftDTO.getModel().length() > 50) {
                errors.add("The new model of an aircraft must be between 2 and 50 characters");
            } else {
                aircraft.setModel(updateAircraftDTO.getModel());
            }
        }

        if (updateAircraftDTO.getManufacturer_serial_number() != null) {
            if (updateAircraftDTO.getManufacturer_serial_number().isBlank() ||
                    updateAircraftDTO.getManufacturer_serial_number().length() < 2 ||
                    updateAircraftDTO.getManufacturer_serial_number().length() > 50) {
                errors.add("The new manufacturer serial number of an aircraft must be between 2 and 50 characters");
            } else {
                aircraft.setManufacturer_serial_number(updateAircraftDTO.getManufacturer_serial_number());
            }
        }

        if (updateAircraftDTO.getNumber_of_engines() != null) {
            if (updateAircraftDTO.getNumber_of_engines() < 1 ||
            updateAircraftDTO.getNumber_of_engines() > 20) {
                errors.add("The aircraft can have at least 1 and at most 20 engines");
            } else {
                aircraft.setNumber_of_engines(updateAircraftDTO.getNumber_of_engines());
            }
        }

        return errors;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
