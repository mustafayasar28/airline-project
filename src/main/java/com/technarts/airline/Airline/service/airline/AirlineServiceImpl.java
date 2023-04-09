package com.technarts.airline.Airline.service.airline;

import com.technarts.airline.Airline.exception.airline.AirlineNameUniqueException;
import com.technarts.airline.Airline.exception.airline.AirlineNotFoundException;
import com.technarts.airline.Airline.exception.airline.update.AirlineUpdateFieldNotValidException;
import com.technarts.airline.Airline.model.dto.aircraft.AircraftDTO;
import com.technarts.airline.Airline.model.dto.airline.AirlineDTO;
import com.technarts.airline.Airline.model.dto.airline.CreateAirlineDTO;
import com.technarts.airline.Airline.model.dto.airline.UpdateAirlineDTO;
import com.technarts.airline.Airline.model.entity.Aircraft;
import com.technarts.airline.Airline.model.entity.Airline;
import com.technarts.airline.Airline.model.entity.User;
import com.technarts.airline.Airline.repository.AircraftRepository;
import com.technarts.airline.Airline.repository.AirlineRepository;
import com.technarts.airline.Airline.repository.UserRepository;
import com.technarts.airline.Airline.service.aircraft.AircraftServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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
public class AirlineServiceImpl implements AirlineService {
    private final AirlineRepository airlineRepository;
    private final AircraftRepository aircraftRepository;
    private final UserRepository userRepository;
    public AirlineServiceImpl(AirlineRepository airlineRepository, AircraftRepository aircraftRepository, UserRepository userRepository) {
        this.airlineRepository = airlineRepository;
        this.aircraftRepository = aircraftRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AirlineDTO createAirline(CreateAirlineDTO createAirlineDTO) {
        User currentUser = getCurrentUser();

        Airline airlineEntity = Airline.builder()
                .base_airport(createAirlineDTO.getBase_airport())
                .callsign(createAirlineDTO.getCallsign())
                .founded_year(createAirlineDTO.getFounded_year())
                .name(createAirlineDTO.getName())
                .creator(currentUser)
                .build();

        try {
            airlineEntity = airlineRepository.save(airlineEntity);
            log.info("Airline created with id: " + airlineEntity.getId());
        } catch (Exception exception) {
            throw new AirlineNameUniqueException(
                    "The airline name already exists: " + createAirlineDTO.getName()
            );
        }
        return getAirlineDTO(airlineEntity);
    }



    @Override
    public ResponseEntity<AirlineDTO> updateAirline(UUID id, UpdateAirlineDTO updateAirlineDTO) {
        Airline airline = airlineRepository.findById(id)
                .orElseThrow(() -> new AirlineNotFoundException("Airline not found with ID: " + id));

        User currentUser = getCurrentUser();

        if (!airline.getCreator().equals(currentUser)) {
            throw new RuntimeException("Only the creator of the airline can update the airline");
        }

        List<String> errors = updateAirlineFields(updateAirlineDTO, airline);

        if (!errors.isEmpty())
            throw new AirlineUpdateFieldNotValidException(
                    "Update of the airline failed", errors);

        try {
            airlineRepository.save(airline);
            log.info("The airline with id: " + airline.getId() + " has been updated");
        } catch (Exception e) {
            if (e instanceof DataIntegrityViolationException)
                throw new AirlineNameUniqueException(
                        "The airline name already exists: " + updateAirlineDTO.getName()
                );
        }

        return new ResponseEntity<>(getAirlineDTO(airline), HttpStatus.OK);
    }




    @Override
    public AirlineDTO retrieveAirline(UUID id) {
        Airline airline = airlineRepository.findById(id)
                .orElseThrow(() -> new AirlineNotFoundException("Airline not found with ID: " + id));

        return getAirlineDTO(airline);
    }

    /*
    It would be better if we returned CustomPage instead of the list.
    So that we can retrieve the data page by page with Pageable request
     */
    @Override
    public List<AirlineDTO> retrieveAllAirlines() {
        List<Airline> airlineList =
                airlineRepository.findAll();

        return airlineList.stream().map(AirlineServiceImpl::getAirlineDTO).collect(Collectors.toList());
    }

    @Override
    public List<AircraftDTO> retrieveAllAircraftsOfAirline(UUID airlineId) {
        airlineRepository.findById(airlineId).orElseThrow(() -> new AirlineNotFoundException(
                        "Airline with id " + airlineId + " not found"
                ));

        List<Aircraft> aircraftList = aircraftRepository
                .findAircraftsByAirlineId(airlineId);

        return aircraftList.stream().map(AircraftServiceImpl::getAircraftDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteAirline(UUID airlineId) {
        Airline airline = airlineRepository
                .findById(airlineId).orElseThrow(() -> new AirlineNotFoundException(
                        "Airline with id " + airlineId + " not found"
                ));

        User currentUser = getCurrentUser();

        if (!airline.getCreator().equals(currentUser)) {
            throw new RuntimeException("Only the creator of the airline can delete the airline");
        }

        try {
            airlineRepository.delete(airline);
            log.info("The airline with id " + airlineId + " has been deleted");
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while deleting the airline with id: " +
                    airlineId + " " + e.getMessage());
        }
    }

    private static List<String> updateAirlineFields(UpdateAirlineDTO updateAirlineDTO, Airline airline) {
        List<String> errors = new ArrayList<>();

        if (updateAirlineDTO.getName() != null) {
            if (updateAirlineDTO.getName().isBlank() ||
                    updateAirlineDTO.getName().length() < 2 ||
                    updateAirlineDTO.getName().length() > 50) {
                errors.add("The new name of an airline must be between 2 and 50 characters");
            } else {
                airline.setName(updateAirlineDTO.getName());
            }
        }
        if (updateAirlineDTO.getCallsign() != null) {
            if (updateAirlineDTO.getCallsign().isBlank() ||
                    updateAirlineDTO.getCallsign().length() < 2 ||
                    updateAirlineDTO.getCallsign().length() > 50) {
                errors.add("The new callsign of an airline must be between 2 and 50 characters");
            } else {
                airline.setCallsign(updateAirlineDTO.getCallsign());
            }
        }
        if (updateAirlineDTO.getFounded_year() != null) {
            if (updateAirlineDTO.getFounded_year() < 1700 ||
                    updateAirlineDTO.getFounded_year() > 2023) {
                errors.add("New founded year must be between 1700 and 2023");
            } else {
                airline.setFounded_year(updateAirlineDTO.getFounded_year());
            }
        }
        if (updateAirlineDTO.getBase_airport() != null) {
            if (updateAirlineDTO.getBase_airport().isBlank() ||
                    updateAirlineDTO.getBase_airport().length() < 2 ||
                    updateAirlineDTO.getBase_airport().length() > 50) {
                errors.add(
                        "The new base airport of an airline must be between 2 and 50 characters"
                );
            } else {
                airline.setBase_airport(updateAirlineDTO.getCallsign());
            }
        }
        return errors;
    }

    private static AirlineDTO getAirlineDTO(Airline airline) {
        return AirlineDTO.builder()
                .id(airline.getId())
                .name(airline.getName())
                .founded_year(airline.getFounded_year())
                .callsign(airline.getCallsign())
                .base_airport(airline.getBase_airport())
                .build();
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return currentUser;
    }
}
