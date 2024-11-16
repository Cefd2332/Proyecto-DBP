package org.e2e.e2e.CitaVeterinaria;

import java.time.LocalDateTime;

public class CitaVeterinariaResponseDto {

    private Long id;
    private LocalDateTime fechaCita;
    private String veterinario;
    private String motivo;
    private Long animalId;
    private EstadoCita estado;

    // Constructor por defecto
    public CitaVeterinariaResponseDto() {
    }

    // Constructor con todos los campos
    public CitaVeterinariaResponseDto(Long id, LocalDateTime fechaCita, String veterinario, String motivo, Long animalId, EstadoCita estado) {
        this.id = id;
        this.fechaCita = fechaCita;
        this.veterinario = veterinario;
        this.motivo = motivo;
        this.animalId = animalId;
        this.estado = estado;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(LocalDateTime fechaCita) {
        this.fechaCita = fechaCita;
    }

    public String getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(String veterinario) {
        this.veterinario = veterinario;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }

    public EstadoCita getEstado() {
        return estado;
    }

    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "CitaVeterinariaResponseDto{" +
                "id=" + id +
                ", fechaCita=" + fechaCita +
                ", veterinario='" + veterinario + '\'' +
                ", motivo='" + motivo + '\'' +
                ", animalId=" + animalId +
                ", estado=" + estado +
                '}';
    }
}
