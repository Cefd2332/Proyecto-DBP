package org.e2e.e2e.Animal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/animales")
@RequiredArgsConstructor
public class AnimalController {

    private final AnimalService animalService;

    // Obtener todos los animales
    @GetMapping
    public ResponseEntity<List<AnimalResponseDto>> obtenerAnimales() {
        List<AnimalResponseDto> animales = animalService.obtenerTodosLosAnimales().stream()
                .map(animalService::convertirAnimalAResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(animales);
    }

    // Registrar un nuevo animal
    @PostMapping
    public ResponseEntity<AnimalResponseDto> registrarAnimal(@Valid @RequestBody AnimalRequestDto animalDto) {
        Animal animal = animalService.guardarAnimal(animalDto);
        return ResponseEntity.ok(animalService.convertirAnimalAResponseDto(animal));
    }

    // Obtener un animal por ID
    @GetMapping("/{id}")
    public ResponseEntity<AnimalResponseDto> obtenerAnimalPorId(@PathVariable Long id) {
        Animal animal = animalService.obtenerAnimalPorId(id);
        return ResponseEntity.ok(animalService.convertirAnimalAResponseDto(animal));
    }

    // Actualizar un animal
    @PutMapping("/{id}")
    public ResponseEntity<AnimalResponseDto> actualizarAnimal(@PathVariable Long id, @Valid @RequestBody AnimalRequestDto animalDto) {
        Animal animalActualizado = animalService.actualizarAnimal(id, animalDto);
        return ResponseEntity.ok(animalService.convertirAnimalAResponseDto(animalActualizado));
    }

    // Eliminar un animal
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAnimal(@PathVariable Long id) {
        animalService.eliminarAnimal(id);
        return ResponseEntity.noContent().build();
    }

    // Actualizar solo el estado del animal
    @PatchMapping("/{id}/estado")
    public ResponseEntity<AnimalResponseDto> actualizarEstadoAnimal(@PathVariable Long id, @RequestBody EstadoAnimal nuevoEstado) {
        Animal animal = animalService.actualizarEstadoAnimal(id, nuevoEstado);
        return ResponseEntity.ok(animalService.convertirAnimalAResponseDto(animal));
    }
}
