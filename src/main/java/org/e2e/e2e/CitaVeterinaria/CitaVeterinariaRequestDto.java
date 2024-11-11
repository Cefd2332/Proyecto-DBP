package org.e2e.e2e.CitaVeterinaria;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CitaVeterinariaRequestDto {

    @NotNull(message = "La fecha de la cita no puede ser nula")
    @Future(message = "La fecha de la cita debe estar en el futuro")
    private LocalDateTime fechaCita;

    @NotBlank(message = "El veterinario no puede estar vacío")
    private String veterinario;

    @NotNull(message = "El ID del animal no puede ser nulo")
    private Long animalId;

    private EstadoCita estado = EstadoCita.PENDIENTE;

    // Getters y Setters
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
        return "CitaVeterinariaRequestDto{" +
                "fechaCita=" + fechaCita +
                ", veterinario='" + veterinario + '\'' +
                ", animalId=" + animalId +
                ", estado=" + estado +
                '}';
    }
}
