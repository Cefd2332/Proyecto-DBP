package org.e2e.e2e.Adopcion;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/adopciones")
@RequiredArgsConstructor
public class AdopcionController {

    private final AdopcionService adopcionService;

    @GetMapping
    public ResponseEntity<List<Adopcion>> obtenerAdopciones() {
        return ResponseEntity.ok(adopcionService.obtenerTodasLasAdopciones());
    }

    @PostMapping
    public ResponseEntity<Adopcion> registrarAdopcion(@RequestBody Adopcion adopcion) {
        return ResponseEntity.ok(adopcionService.registrarAdopcion(adopcion));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Adopcion> obtenerAdopcionPorId(@PathVariable Long id) {
        return ResponseEntity.ok(adopcionService.obtenerAdopcionPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAdopcion(@PathVariable Long id) {
        adopcionService.eliminarAdopcion(id);
        return ResponseEntity.noContent().build();
    }
}
