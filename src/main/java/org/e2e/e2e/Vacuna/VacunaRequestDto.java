package org.e2e.e2e.Vacuna;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class VacunaRequestDto {

    @NotBlank(message = "El nombre de la vacuna no puede estar vacío")
    private String nombre;

    @NotNull(message = "La fecha de aplicación no puede ser nula")
    private LocalDate fechaAplicacion;

    @NotNull(message = "El ID del animal no puede ser nulo")
    private Long animalId;  // ID del animal al que se le aplicó la vacuna
}
