package org.e2e.e2e.Adopcion;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AdopcionRequestDto {

    @NotNull(message = "La fecha de adopción no puede ser nula")
    private LocalDate fechaAdopcion;

    @NotNull(message = "El ID del adoptante no puede ser nulo")
    private Long adoptanteId;  // ID del usuario que está adoptando

    @NotNull(message = "El ID del animal no puede ser nulo")
    private Long animalId;  // ID del animal que está siendo adoptado
}
