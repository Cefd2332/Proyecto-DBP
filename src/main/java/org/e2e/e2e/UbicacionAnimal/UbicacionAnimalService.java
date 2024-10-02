package org.e2e.e2e.UbicacionAnimal;

import lombok.RequiredArgsConstructor;
import org.e2e.e2e.Animal.Animal;
import org.e2e.e2e.Animal.AnimalService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UbicacionAnimalService {

    private final UbicacionAnimalRepository ubicacionAnimalRepository;
    private final AnimalService animalService;

    public List<UbicacionAnimal> obtenerUbicaciones(Long animalId) {
        Animal animal = animalService.obtenerAnimalPorId(animalId);
        return animal.getUbicaciones();
    }

    public UbicacionAnimal guardarUbicacion(UbicacionAnimal ubicacionAnimal) {
        return ubicacionAnimalRepository.save(ubicacionAnimal);
    }
}
