package org.e2e.e2e.Adopcion;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/adopciones")
@RequiredArgsConstructor
public class AdopcionController {

    private final AdopcionService adopcionService;

    @GetMapping
    public ResponseEntity<List<AdopcionResponseDto>> obtenerAdopciones() {
        List<AdopcionResponseDto> adopciones = adopcionService.obtenerTodasLasAdopciones().stream()
                .map(adopcionService::convertirAdopcionAResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(adopciones);
    }

    @PostMapping
    public ResponseEntity<AdopcionResponseDto> registrarAdopcion(@Valid @RequestBody AdopcionRequestDto adopcionDto) {
        Adopcion adopcion = adopcionService.registrarAdopcion(adopcionDto);
        return ResponseEntity.ok(adopcionService.convertirAdopcionAResponseDto(adopcion));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdopcionResponseDto> obtenerAdopcionPorId(@PathVariable Long id) {
        Adopcion adopcion = adopcionService.obtenerAdopcionPorId(id);
        return ResponseEntity.ok(adopcionService.convertirAdopcionAResponseDto(adopcion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAdopcion(@PathVariable Long id) {
        adopcionService.eliminarAdopcion(id);
        return ResponseEntity.noContent().build();
    }
}
