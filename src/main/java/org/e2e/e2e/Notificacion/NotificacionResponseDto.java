package org.e2e.e2e.Notificacion;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificacionResponseDto {

    private Long id;
    private String mensaje;
    private LocalDateTime fechaEnvio;
    private boolean enviada;
    private Long usuarioId;
}
