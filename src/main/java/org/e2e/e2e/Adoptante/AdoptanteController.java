package org.e2e.e2e.Adoptante;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/adoptantes")
public class AdoptanteController {

    private final AdoptanteService adoptanteService;

    @Autowired
    public AdoptanteController(AdoptanteService adoptanteService) {
        this.adoptanteService = adoptanteService;
    }

    /**
     * Crear un nuevo Adoptante.
     */
    @PostMapping
    public ResponseEntity<AdoptanteResponseDto> crearAdoptante(@RequestBody AdoptanteRequestDto adoptanteRequestDto) {
        Adoptante adoptante = adoptanteService.crearAdoptante(adoptanteRequestDto);
        AdoptanteResponseDto responseDto = mapToResponseDto(adoptante);
        return ResponseEntity.status(201).body(responseDto);
    }

    /**
     * Obtener todos los Adoptantes.
     */
    @GetMapping
    public ResponseEntity<List<AdoptanteResponseDto>> obtenerTodosLosAdoptantes() {
        List<Adoptante> adoptantes = adoptanteService.obtenerTodosLosAdoptantes();
        List<AdoptanteResponseDto> responseDtos = adoptantes.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    /**
     * Obtener un Adoptante por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdoptanteResponseDto> obtenerAdoptantePorId(@PathVariable Long id) {
        Adoptante adoptante = adoptanteService.obtenerAdoptantePorId(id);
        AdoptanteResponseDto responseDto = mapToResponseDto(adoptante);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Actualizar un Adoptante existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AdoptanteResponseDto> actualizarAdoptante(@PathVariable Long id, @RequestBody AdoptanteRequestDto adoptanteRequestDto) {
        Adoptante adoptante = adoptanteService.actualizarAdoptante(id, adoptanteRequestDto);
        AdoptanteResponseDto responseDto = mapToResponseDto(adoptante);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Eliminar un Adoptante.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAdoptante(@PathVariable Long id) {
        adoptanteService.eliminarAdoptante(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Método para mapear Adoptante a AdoptanteResponseDto.
     */
    private AdoptanteResponseDto mapToResponseDto(Adoptante adoptante) {
        return new AdoptanteResponseDto(
                adoptante.getId(),
                adoptante.getNombre(),
                adoptante.getEmail(),
                adoptante.getDireccion(),
                adoptante.getTelefono(),
                adoptante.getRoles().stream().collect(Collectors.toList()),
                adoptante.getDeviceToken() // Incluimos el deviceToken
        );
    }
}
