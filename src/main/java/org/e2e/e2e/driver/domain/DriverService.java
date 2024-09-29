package org.e2e.e2e.driver.domain;


import org.e2e.e2e.coordinate.infrastructure.CoordinateRepository;
import org.e2e.e2e.driver.dto.DriverResponseDto;
import org.e2e.e2e.driver.dto.NewDriverInfoDto;
import org.e2e.e2e.driver.infrastructure.DriverRepository;
import org.e2e.e2e.exceptions.ResourceNotFoundException;
import org.e2e.e2e.passenger.exceptions.UnauthorizeOperationException;
import org.e2e.e2e.vehicle.domain.Vehicle;
import org.e2e.e2e.vehicle.domain.VehicleService;
import org.e2e.e2e.vehicle.dto.VehicleBasicDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class DriverService {
    private final DriverRepository driverRepository;
    private final VehicleService vehicleService;
    private final ModelMapper modelMapper;

    @Autowired
    public DriverService(DriverRepository driverRepository, VehicleService vehicleService) {
        this.driverRepository = driverRepository;
        this.vehicleService = vehicleService;
        this.modelMapper = new ModelMapper();
    }

    public DriverResponseDto getDriverInfo(Long id) {
        Driver driver = driverRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

        DriverResponseDto response = new DriverResponseDto();
        response.setId(driver.getId());
        response.setCategory(driver.getCategory());
        response.setFirstName(driver.getFirstName());
        response.setLastName(driver.getLastName());
        response.setTrips(driver.getTrips());
        response.setAvgRating(driver.getAvgRating());

        Vehicle vehicle = driver.getVehicle();

        VehicleBasicDto vehicleDto = new VehicleBasicDto();
        vehicleDto.setBrand(vehicle.getBrand());
        vehicleDto.setModel(vehicle.getModel());
        vehicleDto.setLicensePlate(vehicle.getLicensePlate());
        vehicleDto.setFabricationYear(vehicle.getFabricationYear());
        vehicleDto.setCapacity(vehicle.getCapacity());

        response.setVehicle(vehicleDto);

        return response;
    }

    public DriverResponseDto getDriverOwnInfo() {
        // Here get the current user identifier (email) using Spring Security

        String username = "email";

        Driver driver = driverRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Driver not found"));

        return getDriverInfo(driver.getId());
    }

    public void deleteDriver(Long id) {
        // Check that the current user is the same as the owner of the resource

        if (!driverRepository.existsById(id))
            throw new ResourceNotFoundException("Driver not found");

        driverRepository.deleteById(id);
    }

    public void updateDriverInfo(Long id, NewDriverInfoDto driverInfo) {
        // Check that the current user is the same as the owner of the resource

        Driver driver = driverRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

        driver.setFirstName(driverInfo.getFirstName());
        driver.setLastName(driverInfo.getLastName());
        driver.setPhoneNumber(driverInfo.getPhoneNumber());

        driverRepository.save(driver);

    }

    public void updateDriverCar(Long id, VehicleBasicDto newVehicle) {
        // Check that the current user is the same as the owner of the resource

        Driver driver = driverRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

        Long vehicleId = driver.getVehicle().getId();
        Vehicle vehicle = vehicleService.getVehicle(vehicleId);

        vehicle.setModel(newVehicle.getModel());
        vehicle.setLicensePlate(newVehicle.getLicensePlate());
        vehicle.setFabricationYear(newVehicle.getFabricationYear());
        vehicle.setCapacity(newVehicle.getCapacity());

        vehicleService.saveVehicle(vehicle);
    }
}
