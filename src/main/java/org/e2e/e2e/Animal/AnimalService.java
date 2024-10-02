package org.e2e.e2e.Animal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimalService {

    private final AnimalRepository animalRepository;

    public List<Animal> obtenerTodosLosAnimales() {
        return animalRepository.findAll();
    }

    public Animal guardarAnimal(Animal animal) {
        return animalRepository.save(animal);
    }

    public Animal obtenerAnimalPorId(Long id) {
        return animalRepository.findById(id).orElseThrow(() -> new RuntimeException("Animal no encontrado"));
    }

    public Animal actualizarAnimal(Long id, Animal animalActualizado) {
        Animal animal = obtenerAnimalPorId(id);
        animal.setNombre(animalActualizado.getNombre());
        animal.setEspecie(animalActualizado.getEspecie());
        animal.setEdad(animalActualizado.getEdad());
        animal.setEstadoSalud(animalActualizado.getEstadoSalud());
        return animalRepository.save(animal);
    }

    public void eliminarAnimal(Long id) {
        animalRepository.deleteById(id);
    }
}
