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

    @GetMapping
    public ResponseEntity<List<AnimalResponseDto>> obtenerAnimales() {
        List<AnimalResponseDto> animales = animalService.obtenerTodosLosAnimales().stream()
                .map(animalService::convertirAnimalAResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(animales);
    }

    @PostMapping
    public ResponseEntity<AnimalResponseDto> registrarAnimal(@Valid @RequestBody AnimalRequestDto animalDto) {
        Animal animal = animalService.guardarAnimal(animalDto);
        return ResponseEntity.ok(animalService.convertirAnimalAResponseDto(animal));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimalResponseDto> obtenerAnimalPorId(@PathVariable Long id) {
        Animal animal = animalService.obtenerAnimalPorId(id);
        return ResponseEntity.ok(animalService.convertirAnimalAResponseDto(animal));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnimalResponseDto> actualizarAnimal(@PathVariable Long id, @Valid @RequestBody AnimalRequestDto animalDto) {
        Animal animalActualizado = animalService.actualizarAnimal(id, animalDto);
        return ResponseEntity.ok(animalService.convertirAnimalAResponseDto(animalActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAnimal(@PathVariable Long id) {
        animalService.eliminarAnimal(id);
        return ResponseEntity.noContent().build();
    }
}
