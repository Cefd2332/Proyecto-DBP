package org.e2e.e2e.Notificacion;

import java.time.LocalDateTime;

public class NotificacionResponseDto {

    private Long id;
    private String mensaje;
    private LocalDateTime fechaEnvio;
    private boolean enviada;
    private Long usuarioId;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public boolean isEnviada() {
        return enviada;
    }

    public void setEnviada(boolean enviada) {
        this.enviada = enviada;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    @Override
    public String toString() {
        return "NotificacionResponseDto{" +
                "id=" + id +
                ", mensaje='" + mensaje + '\'' +
                ", fechaEnvio=" + fechaEnvio +
                ", enviada=" + enviada +
                ", usuarioId=" + usuarioId +
                '}';
    }
}
