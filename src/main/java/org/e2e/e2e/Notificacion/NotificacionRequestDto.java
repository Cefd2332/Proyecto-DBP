package org.e2e.e2e.Notificacion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 * DTO para la solicitud de envío de una notificación.
 */
public class NotificacionRequestDto {

    @NotBlank(message = "El mensaje no puede estar vacío")
    private String mensaje;

    @NotNull(message = "El ID del adoptante no puede ser nulo")
    private Long adoptanteId;

    // Constructores

    /**
     * Constructor por defecto.
     */
    public NotificacionRequestDto() {
        // Constructor vacío necesario para la deserialización
    }

    /**
     * Constructor parametrizado.
     *
     * @param mensaje     Mensaje de la notificación.
     * @param adoptanteId ID del adoptante asociado.
     */
    public NotificacionRequestDto(String mensaje, Long adoptanteId) {
        this.mensaje = mensaje;
        this.adoptanteId = adoptanteId;
    }

    // Getters y Setters

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
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

        NotificacionRequestDto that = (NotificacionRequestDto) o;

        return Objects.equals(mensaje, that.mensaje) &&
                Objects.equals(adoptanteId, that.adoptanteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mensaje, adoptanteId);
    }

    // Método toString para facilitar la depuración

    @Override
    public String toString() {
        return "NotificacionRequestDto{" +
                "mensaje='" + mensaje + '\'' +
                ", adoptanteId=" + adoptanteId +
                '}';
    }
}
