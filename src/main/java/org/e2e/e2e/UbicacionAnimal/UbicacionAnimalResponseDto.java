package org.e2e.e2e.UbicacionAnimal;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UbicacionAnimalResponseDto {

    private Long id;
    private Double latitud;
    private Double longitud;
    private LocalDateTime fechaHora;  // Fecha y hora en la que se registró la ubicación
    private Long animalId;  // ID del animal asociado
}
