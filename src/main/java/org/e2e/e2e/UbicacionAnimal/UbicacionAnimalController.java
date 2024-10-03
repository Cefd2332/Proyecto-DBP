package org.e2e.e2e.UbicacionAnimal;

import jakarta.validation.Valid;
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
    public ResponseEntity<List<UbicacionAnimalResponseDto>> obtenerUbicaciones(@PathVariable Long animalId) {
        List<UbicacionAnimalResponseDto> ubicaciones = ubicacionAnimalService.obtenerUbicaciones(animalId);
        return ResponseEntity.ok(ubicaciones);
    }

    @PostMapping
    public ResponseEntity<UbicacionAnimalResponseDto> registrarUbicacion(@Valid @RequestBody UbicacionAnimalRequestDto ubicacionDto) {
        UbicacionAnimalResponseDto responseDto = ubicacionAnimalService.guardarUbicacion(ubicacionDto);
        return ResponseEntity.ok(responseDto);
    }
}
