package org.e2e.e2e.Vacuna;

import java.time.LocalDate;

public class VacunaResponseDto {

    private Long id;
    private String nombre;
    private LocalDate fechaAplicacion;
    private Long animalId;

    // Constructores
    public VacunaResponseDto() {
    }

    public VacunaResponseDto(Long id, String nombre, LocalDate fechaAplicacion, Long animalId) {
        this.id = id;
        this.nombre = nombre;
        this.fechaAplicacion = fechaAplicacion;
        this.animalId = animalId;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
        return "VacunaResponseDto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", fechaAplicacion=" + fechaAplicacion +
                ", animalId=" + animalId +
                '}';
    }
}
