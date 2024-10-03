package org.e2e.e2e.UbicacionAnimal;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UbicacionAnimalRequestDto {

    @NotNull(message = "La latitud no puede ser nula")
    private Double latitud;

    @NotNull(message = "La longitud no puede ser nula")
    private Double longitud;

    @NotNull(message = "El ID del animal no puede ser nulo")
    private Long animalId;  // ID del animal al que se le asigna la ubicación
}
