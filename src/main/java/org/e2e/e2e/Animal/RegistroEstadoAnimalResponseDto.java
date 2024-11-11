package org.e2e.e2e.Animal;

import java.time.LocalDateTime;

public class RegistroEstadoAnimalResponseDto {

    private Long id;
    private EstadoAnimal estado;
    private LocalDateTime fechaCambio;
    private Long animalId;

    // Constructores
    public RegistroEstadoAnimalResponseDto() {
    }

    public RegistroEstadoAnimalResponseDto(Long id, EstadoAnimal estado, LocalDateTime fechaCambio, Long animalId) {
        this.id = id;
        this.estado = estado;
        this.fechaCambio = fechaCambio;
        this.animalId = animalId;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EstadoAnimal getEstado() {
        return estado;
    }

    public void setEstado(EstadoAnimal estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(LocalDateTime fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public Long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }

    @Override
    public String toString() {
        return "RegistroEstadoAnimalResponseDto{" +
                "id=" + id +
                ", estado=" + estado +
                ", fechaCambio=" + fechaCambio +
                ", animalId=" + animalId +
                '}';
    }
}
