package org.e2e.e2e.Adopcion;

import java.time.LocalDate;

public class AdopcionResponseDto {

    private Long id;
    private LocalDate fechaAdopcion;
    private Long adoptanteId;
    private Long animalId;
    private EstadoAdopcion estado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
        return "AdopcionResponseDto{" +
                "id=" + id +
                ", fechaAdopcion=" + fechaAdopcion +
                ", adoptanteId=" + adoptanteId +
                ", animalId=" + animalId +
                ", estado=" + estado +
                '}';
    }
}
