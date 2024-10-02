package org.e2e.e2e.RegistroSalud;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registro-salud")
@RequiredArgsConstructor
public class RegistroSaludController {

    private final RegistroSaludService registroSaludService;

    @GetMapping("/{animalId}")
    public ResponseEntity<List<RegistroSalud>> obtenerHistorialMedico(@PathVariable Long animalId) {
        return ResponseEntity.ok(registroSaludService.obtenerHistorialMedico(animalId));
    }

    @PostMapping
    public ResponseEntity<RegistroSalud> registrarConsulta(@RequestBody RegistroSalud registroSalud) {
        return ResponseEntity.ok(registroSaludService.guardarRegistroSalud(registroSalud));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRegistroSalud(@PathVariable Long id) {
        registroSaludService.eliminarRegistroSalud(id);
        return ResponseEntity.noContent().build();
    }
}
