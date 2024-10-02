package org.e2e.e2e.UbicacionAnimal;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ubicaciones")
@RequiredArgsConstructor
public class UbicacionAnimalController {

    private final UbicacionAnimalService ubicacionAnimalService;

    @GetMapping("/{animalId}")
    public ResponseEntity<List<UbicacionAnimal>> obtenerUbicaciones(@PathVariable Long animalId) {
        return ResponseEntity.ok(ubicacionAnimalService.obtenerUbicaciones(animalId));
    }

    @PostMapping
    public ResponseEntity<UbicacionAnimal> registrarUbicacion(@RequestBody UbicacionAnimal ubicacionAnimal) {
        return ResponseEntity.ok(ubicacionAnimalService.guardarUbicacion(ubicacionAnimal));
    }
}
