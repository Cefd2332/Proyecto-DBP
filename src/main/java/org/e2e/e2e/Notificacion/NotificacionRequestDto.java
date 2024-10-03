package org.e2e.e2e.Notificacion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotificacionRequestDto {

    @NotBlank(message = "El mensaje no puede estar vacío")
    private String mensaje;

    @NotNull(message = "El ID del usuario no puede ser nulo")
    private Long usuarioId;  // ID del usuario que recibe la notificación
}
