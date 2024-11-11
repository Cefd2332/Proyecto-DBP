package org.e2e.e2e.RegistroSalud;

import java.time.LocalDate;

public class RegistroSaludResponseDto {

    private Long id;
    private String descripcion;
    private LocalDate fechaConsulta;
    private String veterinario;
    private Long animalId;

    public RegistroSaludResponseDto() {
    }

    public RegistroSaludResponseDto(Long id, String descripcion, LocalDate fechaConsulta, String veterinario, Long animalId) {
        this.id = id;
        this.descripcion = descripcion;
        this.fechaConsulta = fechaConsulta;
        this.veterinario = veterinario;
        this.animalId = animalId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaConsulta() {
        return fechaConsulta;
    }

    public void setFechaConsulta(LocalDate fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    public String getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(String veterinario) {
        this.veterinario = veterinario;
    }

    public Long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }

    @Override
    public String toString() {
        return "RegistroSaludResponseDto{" +
                "id=" + id +
                ", descripcion='" + descripcion + '\'' +
                ", fechaConsulta=" + fechaConsulta +
                ", veterinario='" + veterinario + '\'' +
                ", animalId=" + animalId +
                '}';
    }
}
