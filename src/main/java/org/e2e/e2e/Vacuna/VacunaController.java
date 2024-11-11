package org.e2e.e2e.Vacuna;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vacunas")
public class VacunaController {

    private final VacunaService vacunaService;

    // Constructor para inyectar la dependencia VacunaService
    public VacunaController(VacunaService vacunaService) {
        this.vacunaService = vacunaService;
    }

    @GetMapping("/{animalId}")
    public ResponseEntity<List<VacunaResponseDto>> obtenerVacunasPorAnimal(@PathVariable Long animalId) {
        List<VacunaResponseDto> vacunas = vacunaService.obtenerVacunasPorAnimal(animalId).stream()
                .map(vacunaService::convertirVacunaAResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(vacunas);
    }

    @PostMapping
    public ResponseEntity<VacunaResponseDto> registrarVacuna(@Valid @RequestBody VacunaRequestDto vacunaDto) {
        Vacuna vacuna = vacunaService.guardarVacuna(vacunaDto);
        return ResponseEntity.ok(vacunaService.convertirVacunaAResponseDto(vacuna));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVacuna(@PathVariable Long id) {
        vacunaService.eliminarVacuna(id);
        return ResponseEntity.noContent().build();
    }
}
