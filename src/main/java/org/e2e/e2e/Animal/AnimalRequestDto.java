package org.e2e.e2e.Animal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AnimalRequestDto {

    @NotBlank(message = "El nombre del animal no puede estar vacío")
    private String nombre;

    @NotBlank(message = "La especie no puede estar vacía")
    private String especie;

    @Min(value = 0, message = "La edad debe ser un valor positivo")
    private int edad;

    @NotBlank(message = "El estado de salud no puede estar vacío")
    private String estadoSalud;

    @NotNull(message = "El adoptante no puede ser nulo")
    private Long adoptanteId; // Asociado al usuario que adopta al animal
}
