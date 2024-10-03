package org.e2e.e2e.Adopcion;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AdopcionResponseDto {

    private Long id;
    private LocalDate fechaAdopcion;
    private Long adoptanteId;  // ID del usuario adoptante
    private Long animalId;  // ID del animal adoptado
    private EstadoAdopcion estado;  // Estado de la adopción
}
