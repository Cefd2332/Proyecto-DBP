package org.e2e.e2e.Adopcion;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/adopciones")
@RequiredArgsConstructor
public class AdopcionController {

    private final AdopcionService adopcionService;

    // Obtener todas las adopciones
    @GetMapping
    public ResponseEntity<List<AdopcionResponseDto>> obtenerAdopciones() {
        List<AdopcionResponseDto> adopciones = adopcionService.obtenerTodasLasAdopciones().stream()
                .map(adopcionService::convertirAdopcionAResponseDto)
                .toList();
        return ResponseEntity.ok(adopciones);
    }

    // Registrar una adopción
    @PostMapping
    public ResponseEntity<AdopcionResponseDto> registrarAdopcion(@Valid @RequestBody AdopcionRequestDto adopcionDto) {
        Adopcion adopcion = adopcionService.registrarAdopcion(adopcionDto);
        return ResponseEntity.ok(adopcionService.convertirAdopcionAResponseDto(adopcion));
    }

    // Obtener una adopción por ID
    @GetMapping("/{id}")
    public ResponseEntity<AdopcionResponseDto> obtenerAdopcionPorId(@PathVariable Long id) {
        Adopcion adopcion = adopcionService.obtenerAdopcionPorId(id);
        return ResponseEntity.ok(adopcionService.convertirAdopcionAResponseDto(adopcion));
    }

    // Eliminar una adopción por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAdopcion(@PathVariable Long id) {
        adopcionService.eliminarAdopcion(id);
        return ResponseEntity.noContent().build();
    }
}
