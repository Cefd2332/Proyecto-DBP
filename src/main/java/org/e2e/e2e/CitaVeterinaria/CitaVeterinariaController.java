package org.e2e.e2e.CitaVeterinaria;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
}
