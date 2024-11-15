package org.e2e.e2e.CitaVeterinaria;

import java.time.LocalDateTime;

public class CitaVeterinariaResponseDto {

    private Long id;
    private LocalDateTime fechaCita;
    private String veterinario;
    private String motivo;
    private Long animalId; // Asegurarse de que este campo esté presente
    private EstadoCita estado;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public LocalDateTime getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(LocalDateTime fechaCita) {
        this.fechaCita = fechaCita;
    }

    public void setId(Long id) {
        this.id = id;
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
