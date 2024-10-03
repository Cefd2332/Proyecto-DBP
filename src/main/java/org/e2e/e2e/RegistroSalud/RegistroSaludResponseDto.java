package org.e2e.e2e.RegistroSalud;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RegistroSaludResponseDto {

    private Long id;
    private String descripcion;
    private LocalDate fechaConsulta;
    private String veterinario;
    private Long animalId;  // ID del animal asociado
}
