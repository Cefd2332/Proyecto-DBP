package org.e2e.e2e.RegistroSalud;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegistroSaludRequestDto {

    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;

    @NotNull(message = "La fecha de la consulta no puede ser nula")
    private LocalDate fechaConsulta;

    @NotBlank(message = "El nombre del veterinario no puede estar vacío")
    private String veterinario;

    @NotNull(message = "El ID del animal no puede ser nulo")
    private Long animalId;  // ID del animal asociado al registro de salud
}
