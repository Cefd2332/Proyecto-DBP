package org.e2e.e2e.CitaVeterinaria;

import jakarta.validation.Valid;
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

    // Nuevo método para obtener una cita por ID
    @GetMapping("/{id}")
    public ResponseEntity<CitaVeterinariaResponseDto> obtenerCitaPorId(@PathVariable Long id) {
        CitaVeterinaria cita = citaVeterinariaService.obtenerCitaPorId(id);
        if (cita != null) {
            CitaVeterinariaResponseDto citaDto = citaVeterinariaService.convertirCitaAResponseDto(cita);
            return ResponseEntity.ok(citaDto);
        } else {
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
        CitaVeterinaria cita = citaVeterinariaService.guardarCita(citaDto);
        return ResponseEntity.ok(citaVeterinariaService.convertirCitaAResponseDto(cita));
    }

    // Método para eliminar una cita por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCita(@PathVariable Long id) {
        citaVeterinariaService.eliminarCita(id);
        return ResponseEntity.noContent().build();
    }

    // Método para actualizar el estado de una cita
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstadoCita(@PathVariable Long id, @RequestBody EstadoCita nuevoEstado) {
        try {
            CitaVeterinaria citaActualizada = citaVeterinariaService.actualizarEstadoCita(id, nuevoEstado);
            return ResponseEntity.ok(citaVeterinariaService.convertirCitaAResponseDto(citaActualizada));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar el estado de la cita: " + e.getMessage());
        }
    }
}
