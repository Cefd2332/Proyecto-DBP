package org.e2e.e2e.CitaVeterinaria;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CitaVeterinariaRequestDto {

    @NotNull(message = "La fecha de la cita no puede ser nula")
    @Future(message = "La fecha de la cita debe estar en el futuro")
    private LocalDateTime fechaCita;

    @NotBlank(message = "El veterinario no puede estar vacío")
    private String veterinario;

    @NotNull(message = "El ID del animal no puede ser nulo")
    private Long animalId;

    private EstadoCita estado = EstadoCita.PENDIENTE;  // Estado por defecto al crear la cita
}

