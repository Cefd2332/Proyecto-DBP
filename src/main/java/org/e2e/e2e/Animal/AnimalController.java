package org.e2e.e2e.Animal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> registrarAnimal(@Valid @RequestBody AnimalRequestDto animalDto) {
        try {
            Animal animal = animalService.guardarAnimal(animalDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(animalService.convertirAnimalAResponseDto(animal));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al registrar el animal: " + e.getMessage());
        }
    }

    // Obtener un animal por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerAnimalPorId(@PathVariable Long id) {
        try {
            Animal animal = animalService.obtenerAnimalPorId(id);
            return ResponseEntity.ok(animalService.convertirAnimalAResponseDto(animal));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Animal no encontrado con ID: " + id);
        }
    }

    // Actualizar un animal
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarAnimal(@PathVariable Long id, @Valid @RequestBody AnimalRequestDto animalDto) {
        try {
            Animal animalActualizado = animalService.actualizarAnimal(id, animalDto);
            return ResponseEntity.ok(animalService.convertirAnimalAResponseDto(animalActualizado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar el animal: " + e.getMessage());
        }
    }

    // Eliminar un animal
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarAnimal(@PathVariable Long id) {
        try {
            animalService.eliminarAnimal(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Animal no encontrado con ID: " + id);
        }
    }

    // Actualizar solo el estado del animal
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstadoAnimal(@PathVariable Long id, @RequestBody EstadoAnimal nuevoEstado) {
        try {
            Animal animal = animalService.actualizarEstadoAnimal(id, nuevoEstado);
            return ResponseEntity.ok(animalService.convertirAnimalAResponseDto(animal));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar el estado del animal: " + e.getMessage());
        }
    }

    // Obtener el historial de cambios de estado del animal
    @GetMapping("/{id}/historial-estados")
    public ResponseEntity<?> obtenerHistorialEstados(@PathVariable Long id) {
        try {
            List<RegistroEstadoAnimalResponseDto> historial = animalService.obtenerHistorialEstados(id);
            return ResponseEntity.ok(historial);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Historial de estados no encontrado para el animal con ID: " + id);
        }
    }
}
