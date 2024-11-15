package org.e2e.e2e.Notificacion;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * DTO para la respuesta de una notificación.
 */
public class NotificacionResponseDto {

    private Long id;
    private String mensaje;
    private LocalDateTime fechaEnvio;
    private boolean enviada;
    private Long adoptanteId;

    // Constructores

    /**
     * Constructor por defecto.
     */
    public NotificacionResponseDto() {
        // Constructor vacío necesario para la deserialización
    }

    /**
     * Constructor parametrizado.
     *
     * @param id          ID de la notificación.
     * @param mensaje     Mensaje de la notificación.
     * @param fechaEnvio  Fecha y hora de envío.
     * @param enviada     Estado de la notificación.
     * @param adoptanteId ID del adoptante asociado.
     */
    public NotificacionResponseDto(Long id, String mensaje, LocalDateTime fechaEnvio, boolean enviada, Long adoptanteId) {
        this.id = id;
        this.mensaje = mensaje;
        this.fechaEnvio = fechaEnvio;
        this.enviada = enviada;
        this.adoptanteId = adoptanteId;
    }

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

    public Long getAdoptanteId() {
        return adoptanteId;
    }

    public void setAdoptanteId(Long adoptanteId) {
        this.adoptanteId = adoptanteId;
    }

    // Override de equals y hashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NotificacionResponseDto that = (NotificacionResponseDto) o;

        return enviada == that.enviada &&
                Objects.equals(id, that.id) &&
                Objects.equals(mensaje, that.mensaje) &&
                Objects.equals(fechaEnvio, that.fechaEnvio) &&
                Objects.equals(adoptanteId, that.adoptanteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mensaje, fechaEnvio, enviada, adoptanteId);
    }

    // Método toString para facilitar la depuración

    @Override
    public String toString() {
        return "NotificacionResponseDto{" +
                "id=" + id +
                ", mensaje='" + mensaje + '\'' +
                ", fechaEnvio=" + fechaEnvio +
                ", enviada=" + enviada +
                ", adoptanteId=" + adoptanteId +
                '}';
    }
}
