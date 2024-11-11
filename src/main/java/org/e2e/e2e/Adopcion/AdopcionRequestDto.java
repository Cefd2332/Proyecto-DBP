package org.e2e.e2e.Adopcion;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class AdopcionRequestDto {

    @NotNull(message = "La fecha de adopción no puede ser nula")
    private LocalDate fechaAdopcion;

    @NotNull(message = "El ID del adoptante no puede ser nulo")
    private Long adoptanteId;

    @NotNull(message = "El ID del animal no puede ser nulo")
    private Long animalId;

    private EstadoAdopcion estado = EstadoAdopcion.EN_PROCESO;

    public LocalDate getFechaAdopcion() {
        return fechaAdopcion;
    }

    public void setFechaAdopcion(LocalDate fechaAdopcion) {
        this.fechaAdopcion = fechaAdopcion;
    }

    public Long getAdoptanteId() {
        return adoptanteId;
    }

    public void setAdoptanteId(Long adoptanteId) {
        this.adoptanteId = adoptanteId;
    }

    public Long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }

    public EstadoAdopcion getEstado() {
        return estado;
    }

    public void setEstado(EstadoAdopcion estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "AdopcionRequestDto{" +
                "fechaAdopcion=" + fechaAdopcion +
                ", adoptanteId=" + adoptanteId +
                ", animalId=" + animalId +
                ", estado=" + estado +
                '}';
    }
}
