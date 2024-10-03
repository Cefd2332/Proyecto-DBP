package org.e2e.e2e.RegistroSalud;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/registro-salud")
@RequiredArgsConstructor
public class RegistroSaludController {

    private final RegistroSaludService registroSaludService;

    @GetMapping("/{animalId}")
    public ResponseEntity<List<RegistroSaludResponseDto>> obtenerHistorialMedico(@PathVariable Long animalId) {
        List<RegistroSaludResponseDto> historial = registroSaludService.obtenerHistorialMedico(animalId);
        return ResponseEntity.ok(historial);
    }

    @PostMapping
    public ResponseEntity<RegistroSaludResponseDto> registrarConsulta(@Valid @RequestBody RegistroSaludRequestDto registroSaludDto) {
        RegistroSaludResponseDto responseDto = registroSaludService.guardarRegistroSalud(registroSaludDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRegistroSalud(@PathVariable Long id) {
        registroSaludService.eliminarRegistroSalud(id);
        return ResponseEntity.noContent().build();
    }
}
