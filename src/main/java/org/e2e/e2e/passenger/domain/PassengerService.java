package org.e2e.e2e.passenger.domain;


import org.e2e.e2e.coordinate.domain.Coordinate;
import org.e2e.e2e.coordinate.infrastructure.CoordinateRepository;
import org.e2e.e2e.exceptions.ResourceNotFoundException;
import org.e2e.e2e.passenger.dto.*;
import org.e2e.e2e.passenger.infrastructure.PassengerRepository;
import org.e2e.e2e.user_locations.domain.UserLocation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassengerService {

    private final PassengerRepository passengerRepository;
    private final ModelMapper modelMapper;
    private final CoordinateRepository coordinateRepository;


    @Autowired
    public PassengerService(PassengerRepository passengerRepository, ModelMapper modelMapper, CoordinateRepository coordinateRepository) {
        this.passengerRepository = passengerRepository;
        this.modelMapper = modelMapper;
        this.coordinateRepository = coordinateRepository;
    }

    public PassengerSelfResponseDTO getPassengerOwnInfo() {
        // Here get the current user identifier (email) using Spring Security
        String username = "email";

        Passenger passenger = passengerRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Passenger not found"));
        return modelMapper.map(passenger, PassengerSelfResponseDTO.class);

    }

    public PassengerResponseDTO getPassengerInfo(Long id) {
        Passenger passenger = passengerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Passenger not found"));
        return modelMapper.map(passenger, PassengerResponseDTO.class);
    }

    public void deletePassenger(Long id) {
        // Check that the current user is the same as the owner of the resource
        passengerRepository.deleteById(id);
    }

    public String addPassengerPlace(NewPassengerLocationDTO passengerLocationResponseDTO) {
        // Here get the current user identifier (email) using Spring Security
        String username = "email";

        Passenger passenger = passengerRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Passenger not found"));

        Double longitude = passengerLocationResponseDTO.getLongitude();
        Double latitude = passengerLocationResponseDTO.getLatitude();

        Coordinate coordinate = new Coordinate(latitude, longitude);
        Coordinate newPlace = coordinateRepository.save(coordinate);

        String description = passengerLocationResponseDTO.getDescription();
        passenger.addCoordinate(coordinate, description);

        passengerRepository.save(passenger);

        return "/passenger/places/" + newPlace.getId();

    }

    public List<PassengerLocationResponseDTO> getPassengerPlaces() {
        // Here get the current user identifier (email) using Spring Security
        String username = "email";
        Passenger passenger = passengerRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Passenger not found"));

        List<UserLocation> userLocations = passenger.getCoordinates();

        return userLocations.stream().map(userLocation -> {
            PassengerLocationResponseDTO passengerLocationResponseDTO = new PassengerLocationResponseDTO();

            passengerLocationResponseDTO.setCoordinateId(userLocation.getId().getCoordinateId());
            passengerLocationResponseDTO.setDescription(userLocation.getDescription());
            passengerLocationResponseDTO.setLatitude(userLocation.getCoordinate().getLatitude());
            passengerLocationResponseDTO.setLongitude(userLocation.getCoordinate().getLongitude());
            return passengerLocationResponseDTO;
        }).toList();
    }

    public void deletePassengerPlace(Long coordinateId) {
        // Here get the current user identifier (email) using Spring Security
        String username = "email";

        Passenger passenger = passengerRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Passenger not found"));

        Coordinate coordinate = coordinateRepository
                .findById(coordinateId)
                .orElseThrow(() -> new ResourceNotFoundException("Coordinate not found"));

        passenger.removeCoordinate(coordinate);

        coordinateRepository.delete(coordinate);

        passengerRepository.save(passenger);
    }

    public void updatePassenger(PatchPassengerInfoDto passengerSelfResponseDTO) {
        // Here get the current user identifier (email) using Spring Security
        String username = "email";

        Passenger passenger = passengerRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Passenger not found"));

        passenger.setFirstName(passengerSelfResponseDTO.getFirstName());
        passenger.setLastName(passengerSelfResponseDTO.getLastName());
        passenger.setPhoneNumber(passengerSelfResponseDTO.getPhoneNumber());

        passengerRepository.save(passenger);
    }
}
