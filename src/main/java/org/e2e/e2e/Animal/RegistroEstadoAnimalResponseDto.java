package org.e2e.e2e.Animal;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RegistroEstadoAnimalResponseDto {
    private Long id;  // ID del registro de cambio de estado
    private EstadoAnimal estado;  // El nuevo estado del animal
    private LocalDateTime fechaCambio;  // La fecha y hora en que se cambió el estado
    private Long animalId;  // El ID del animal al que pertenece este registro
}
