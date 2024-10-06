package org.e2e.e2e.CitaVeterinaria;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaVeterinariaController {

    private final CitaVeterinariaService citaVeterinariaService;

    @GetMapping("/{animalId}")
    public ResponseEntity<List<CitaVeterinariaResponseDto>> obtenerCitasPorAnimal(@PathVariable Long animalId) {
        List<CitaVeterinariaResponseDto> citas = citaVeterinariaService.obtenerCitasPorAnimal(animalId).stream()
                .map(citaVeterinariaService::convertirCitaAResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(citas);
    }

    @PostMapping
    public ResponseEntity<CitaVeterinariaResponseDto> registrarCita(@Valid @RequestBody CitaVeterinariaRequestDto citaDto) {
        CitaVeterinaria cita = citaVeterinariaService.guardarCita(citaDto);
        return ResponseEntity.ok(citaVeterinariaService.convertirCitaAResponseDto(cita));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCita(@PathVariable Long id) {
        citaVeterinariaService.eliminarCita(id);
        return ResponseEntity.noContent().build();
    }

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
