package org.e2e.e2e.Adopcion;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/adopciones")
public class AdopcionController {

    private final AdopcionService adopcionService;

    // Constructor que inyecta AdopcionService
    public AdopcionController(AdopcionService adopcionService) {
        this.adopcionService = adopcionService;
    }

    /**
     * Obtener todas las adopciones.
     *
     * @return Lista de adopciones.
     */
    @GetMapping
    public ResponseEntity<List<AdopcionResponseDto>> obtenerAdopciones() {
        List<AdopcionResponseDto> adopciones = adopcionService.obtenerTodasLasAdopciones().stream()
                .map(adopcionService::convertirAdopcionAResponseDto)
                .toList();
        return ResponseEntity.ok(adopciones);
    }

    /**
     * Registrar una nueva adopción.
     *
     * @param adopcionDto DTO con los datos de la adopción.
     * @return DTO de respuesta con los datos de la adopción registrada.
     */
    @PostMapping
    public ResponseEntity<AdopcionResponseDto> registrarAdopcion(@Valid @RequestBody AdopcionRequestDto adopcionDto) {
        Adopcion adopcion = adopcionService.registrarAdopcion(adopcionDto);
        AdopcionResponseDto responseDto = adopcionService.convertirAdopcionAResponseDto(adopcion);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Obtener una adopción por su ID.
     *
     * @param id ID de la adopción.
     * @return DTO de respuesta con los datos de la adopción.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdopcionResponseDto> obtenerAdopcionPorId(@PathVariable Long id) {
        Adopcion adopcion = adopcionService.obtenerAdopcionPorId(id);
        AdopcionResponseDto responseDto = adopcionService.convertirAdopcionAResponseDto(adopcion);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Eliminar una adopción por su ID.
     *
     * @param id ID de la adopción a eliminar.
     * @return Respuesta sin contenido.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAdopcion(@PathVariable Long id) {
        adopcionService.eliminarAdopcion(id);
        return ResponseEntity.noContent().build();
    }
}
