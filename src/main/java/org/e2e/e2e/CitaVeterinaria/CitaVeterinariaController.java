// src/main/java/org/e2e/e2e/CitaVeterinaria/CitaVeterinariaController.java

package org.e2e.e2e.CitaVeterinaria;

import jakarta.validation.Valid;
import org.e2e.e2e.exceptions.BadRequestException;
import org.e2e.e2e.exceptions.ConflictException;
import org.e2e.e2e.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/citas")
public class CitaVeterinariaController {

    private final CitaVeterinariaService citaVeterinariaService;

    // Constructor para la inyección de dependencias
    public CitaVeterinariaController(CitaVeterinariaService citaVeterinariaService) {
        this.citaVeterinariaService = citaVeterinariaService;
    }

    // Método para obtener todas las citas
    @GetMapping
    public ResponseEntity<List<CitaVeterinariaResponseDto>> obtenerTodasLasCitas() {
        List<CitaVeterinariaResponseDto> citas = citaVeterinariaService.obtenerTodasLasCitas().stream()
                .map(citaVeterinariaService::convertirCitaAResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(citas);
    }

    // Método para obtener una cita por ID
    @GetMapping("/{id}")
    public ResponseEntity<CitaVeterinariaResponseDto> obtenerCitaPorId(@PathVariable Long id) {
        try {
            CitaVeterinaria cita = citaVeterinariaService.obtenerCitaPorId(id);
            CitaVeterinariaResponseDto citaDto = citaVeterinariaService.convertirCitaAResponseDto(cita);
            return ResponseEntity.ok(citaDto);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Método para obtener citas por ID de animal
    @GetMapping("/animal/{animalId}")
    public ResponseEntity<List<CitaVeterinariaResponseDto>> obtenerCitasPorAnimal(@PathVariable Long animalId) {
        List<CitaVeterinariaResponseDto> citas = citaVeterinariaService.obtenerCitasPorAnimal(animalId).stream()
                .map(citaVeterinariaService::convertirCitaAResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(citas);
    }

    // Método para registrar una nueva cita
    @PostMapping
    public ResponseEntity<CitaVeterinariaResponseDto> registrarCita(@Valid @RequestBody CitaVeterinariaRequestDto citaDto) {
        try {
            CitaVeterinaria cita = citaVeterinariaService.guardarCita(citaDto);
            CitaVeterinariaResponseDto citaResponseDto = citaVeterinariaService.convertirCitaAResponseDto(cita);
            return ResponseEntity.status(HttpStatus.CREATED).body(citaResponseDto);
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Método para eliminar una cita por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCita(@PathVariable Long id) {
        try {
            citaVeterinariaService.eliminarCita(id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Método para actualizar el estado de una cita
    @PatchMapping("/{id}/estado")
    public ResponseEntity<CitaVeterinariaResponseDto> actualizarEstadoCita(@PathVariable Long id, @RequestBody EstadoCita nuevoEstado) {
        try {
            CitaVeterinaria citaActualizada = citaVeterinariaService.actualizarEstadoCita(id, nuevoEstado);
            CitaVeterinariaResponseDto citaDto = citaVeterinariaService.convertirCitaAResponseDto(citaActualizada);
            return ResponseEntity.ok(citaDto);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Método para actualizar una cita completa
    @PutMapping("/{id}")
    public ResponseEntity<CitaVeterinariaResponseDto> actualizarCita(
            @PathVariable Long id,
            @Valid @RequestBody CitaUpdateDto citaUpdateDto) {
        try {
            CitaVeterinaria citaActualizada = citaVeterinariaService.actualizarCita(id, citaUpdateDto);
            CitaVeterinariaResponseDto citaDto = citaVeterinariaService.convertirCitaAResponseDto(citaActualizada);
            return ResponseEntity.ok(citaDto);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
