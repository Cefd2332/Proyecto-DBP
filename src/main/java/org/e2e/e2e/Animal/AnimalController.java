package org.e2e.e2e.Animal;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/animales")
@RequiredArgsConstructor
public class AnimalController {

    private final AnimalService animalService;

    @GetMapping
    public ResponseEntity<List<Animal>> obtenerAnimales() {
        return ResponseEntity.ok(animalService.obtenerTodosLosAnimales());
    }

    @PostMapping
    public ResponseEntity<Animal> registrarAnimal(@RequestBody Animal animal) {
        return ResponseEntity.ok(animalService.guardarAnimal(animal));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Animal> obtenerAnimalPorId(@PathVariable Long id) {
        return ResponseEntity.ok(animalService.obtenerAnimalPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Animal> actualizarAnimal(@PathVariable Long id, @RequestBody Animal animal) {
        return ResponseEntity.ok(animalService.actualizarAnimal(id, animal));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAnimal(@PathVariable Long id) {
        animalService.eliminarAnimal(id);
        return ResponseEntity.noContent().build();
    }
}
