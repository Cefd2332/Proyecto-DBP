package org.e2e.e2e.Vacuna;

import lombok.RequiredArgsConstructor;
import org.e2e.e2e.Vacuna.Vacuna;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vacunas")
@RequiredArgsConstructor
public class VacunaController {

    private final VacunaService vacunaService;

    @GetMapping("/{animalId}")
    public ResponseEntity<List<Vacuna>> obtenerVacunasPorAnimal(@PathVariable Long animalId) {
        return ResponseEntity.ok(vacunaService.obtenerVacunasPorAnimal(animalId));
    }

    @PostMapping
    public ResponseEntity<Vacuna> registrarVacuna(@RequestBody Vacuna vacuna) {
        return ResponseEntity.ok(vacunaService.guardarVacuna(vacuna));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVacuna(@PathVariable Long id) {
        vacunaService.eliminarVacuna(id);
        return ResponseEntity.noContent().build();
    }
}
