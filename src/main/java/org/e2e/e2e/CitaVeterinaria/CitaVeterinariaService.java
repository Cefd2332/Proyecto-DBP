package org.e2e.e2e.CitaVeterinaria;

import lombok.RequiredArgsConstructor;
import org.e2e.e2e.Animal.Animal;
import org.e2e.e2e.Animal.AnimalService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CitaVeterinariaService {

    private final CitaVeterinariaRepository citaVeterinariaRepository;
    private final AnimalService animalService;

    public List<CitaVeterinaria> obtenerCitasPorAnimal(Long animalId) {
        Animal animal = animalService.obtenerAnimalPorId(animalId);
        return animal.getCitasVeterinarias();
    }

    public CitaVeterinaria guardarCita(CitaVeterinaria citaVeterinaria) {
        return citaVeterinariaRepository.save(citaVeterinaria);
    }

    public void eliminarCita(Long id) {
        citaVeterinariaRepository.deleteById(id);
    }
}
