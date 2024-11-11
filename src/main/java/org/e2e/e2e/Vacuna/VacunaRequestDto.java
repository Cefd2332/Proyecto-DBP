package org.e2e.e2e.Vacuna;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class VacunaRequestDto {

    @NotBlank(message = "El nombre de la vacuna no puede estar vacío")
    private String nombre;

    @NotNull(message = "La fecha de aplicación no puede ser nula")
    private LocalDate fechaAplicacion;

    @NotNull(message = "El ID del animal no puede ser nulo")
    private Long animalId;

    // Constructores
    public VacunaRequestDto() {
    }

    public VacunaRequestDto(String nombre, LocalDate fechaAplicacion, Long animalId) {
        this.nombre = nombre;
        this.fechaAplicacion = fechaAplicacion;
        this.animalId = animalId;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(LocalDate fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }

    public Long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }

    @Override
    public String toString() {
        return "VacunaRequestDto{" +
                "nombre='" + nombre + '\'' +
                ", fechaAplicacion=" + fechaAplicacion +
                ", animalId=" + animalId +
                '}';
    }
}
