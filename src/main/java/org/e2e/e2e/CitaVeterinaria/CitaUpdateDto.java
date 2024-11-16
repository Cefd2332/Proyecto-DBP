// src/main/java/org/e2e/e2e/CitaVeterinaria/CitaUpdateDto.java

package org.e2e.e2e.CitaVeterinaria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CitaUpdateDto {

    @NotNull(message = "La fecha de la cita no puede ser nula")
    private LocalDateTime fechaCita;

    @NotBlank(message = "El motivo no puede estar vacío")
    private String motivo;

    @NotBlank(message = "El veterinario no puede estar vacío")
    private String veterinario;

    @NotBlank(message = "El estado no puede estar vacío")
    private String estado;

    // Constructor por defecto
    public CitaUpdateDto() {
    }

    // Constructor con todos los campos
    public CitaUpdateDto(LocalDateTime fechaCita, String motivo, String veterinario, String estado) {
        this.fechaCita = fechaCita;
        this.motivo = motivo;
        this.veterinario = veterinario;
        this.estado = estado;
    }

    // Getters y Setters

    public LocalDateTime getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(LocalDateTime fechaCita) {
        this.fechaCita = fechaCita;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(String veterinario) {
        this.veterinario = veterinario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
