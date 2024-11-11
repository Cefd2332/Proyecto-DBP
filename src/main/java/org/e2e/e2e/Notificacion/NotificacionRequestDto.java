package org.e2e.e2e.Notificacion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class NotificacionRequestDto {

    @NotBlank(message = "El mensaje no puede estar vacío")
    private String mensaje;

    @NotNull(message = "El ID del usuario no puede ser nulo")
    private Long usuarioId;

    // Getters y Setters
    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    @Override
    public String toString() {
        return "NotificacionRequestDto{" +
                "mensaje='" + mensaje + '\'' +
                ", usuarioId=" + usuarioId +
                '}';
    }
}
