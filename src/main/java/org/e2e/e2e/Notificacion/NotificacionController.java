package org.e2e.e2e.Notificacion;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;

    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<NotificacionResponseDto>> obtenerNotificacionesPorUsuario(@PathVariable Long usuarioId) {
        List<NotificacionResponseDto> notificaciones = notificacionService.obtenerNotificacionesPorUsuario(usuarioId).stream()
                .map(notificacionService::convertirNotificacionAResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(notificaciones);
    }

    @PostMapping
    public ResponseEntity<NotificacionResponseDto> enviarNotificacion(@Valid @RequestBody NotificacionRequestDto notificacionDto) {
        Notificacion notificacion = notificacionService.guardarNotificacion(notificacionDto);
        return ResponseEntity.ok(notificacionService.convertirNotificacionAResponseDto(notificacion));
    }
}
