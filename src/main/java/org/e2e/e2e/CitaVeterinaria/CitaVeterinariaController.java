package org.e2e.e2e.CitaVeterinaria;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaVeterinariaController {

    private final CitaVeterinariaService citaVeterinariaService;

    @GetMapping("/{animalId}")
    public ResponseEntity<List<CitaVeterinaria>> obtenerCitasPorAnimal(@PathVariable Long animalId) {
        return ResponseEntity.ok(citaVeterinariaService.obtenerCitasPorAnimal(animalId));
    }

    @PostMapping
    public ResponseEntity<CitaVeterinaria> registrarCita(@RequestBody CitaVeterinaria citaVeterinaria) {
        return ResponseEntity.ok(citaVeterinariaService.guardarCita(citaVeterinaria));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCita(@PathVariable Long id) {
        citaVeterinariaService.eliminarCita(id);
        return ResponseEntity.noContent().build();
    }
}
