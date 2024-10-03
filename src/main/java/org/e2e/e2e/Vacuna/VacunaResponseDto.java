package org.e2e.e2e.Vacuna;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VacunaResponseDto {

    private Long id;
    private String nombre;
    private LocalDate fechaAplicacion;
    private Long animalId;  // ID del animal que recibió la vacuna
}
