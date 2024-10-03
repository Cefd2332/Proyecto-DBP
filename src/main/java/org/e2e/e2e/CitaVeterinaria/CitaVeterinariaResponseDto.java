package org.e2e.e2e.CitaVeterinaria;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CitaVeterinariaResponseDto {

    private Long id;
    private LocalDateTime fechaCita;
    private String veterinario;
    private Long animalId; // ID del animal asociado a la cita
    private EstadoCita estado; // Estado de la cita ("pendiente", "realizada", "cancelada")
}
