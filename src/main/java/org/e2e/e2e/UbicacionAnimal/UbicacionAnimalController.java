package org.e2e.e2e.UbicacionAnimal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ubicaciones")
@RequiredArgsConstructor
public class UbicacionAnimalController {

    private final UbicacionAnimalService ubicacionAnimalService;

    @GetMapping("/{animalId}")
    public ResponseEntity<List<UbicacionAnimalResponseDto>> obtenerUbicaciones(@PathVariable Long animalId) {
        List<UbicacionAnimalResponseDto> ubicaciones = ubicacionAnimalService.obtenerUbicaciones(animalId).stream()
                .map(ubicacionAnimalService::convertirUbicacionAResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ubicaciones);
    }

    @PostMapping
    public ResponseEntity<UbicacionAnimalResponseDto> registrarUbicacion(@Valid @RequestBody UbicacionAnimalRequestDto ubicacionDto) {
        UbicacionAnimal ubicacion = ubicacionAnimalService.guardarUbicacion(ubicacionDto);
        return ResponseEntity.ok(ubicacionAnimalService.convertirUbicacionAResponseDto(ubicacion));
    }
}
