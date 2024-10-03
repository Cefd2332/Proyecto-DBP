package org.e2e.e2e.Animal;

import lombok.Data;

@Data
public class AnimalResponseDto {

    private Long id;
    private String nombre;
    private String especie;
    private int edad;
    private String estadoSalud;
    private Long adoptanteId; // ID del adoptante
}
