package org.e2e.e2e.Animal;

import lombok.Data;
import org.e2e.e2e.CitaVeterinaria.CitaVeterinariaResponseDto;
import org.e2e.e2e.RegistroSalud.RegistroSaludResponseDto;
import org.e2e.e2e.UbicacionAnimal.UbicacionAnimalResponseDto;

import java.util.List;

@Data
public class HistorialCompletoAnimalDto {

    private Long animalId;
    private String nombre;
    private String especie;
    private int edad;
    private String estadoSalud;

    // Listas para los diferentes registros del historial
    private List<CitaVeterinariaResponseDto> citasVeterinarias;
    private List<RegistroSaludResponseDto> registrosDeSalud;
    private List<UbicacionAnimalResponseDto> ubicaciones;
}
