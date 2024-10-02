package org.e2e.e2e.Vacuna;

import lombok.RequiredArgsConstructor;
import org.e2e.e2e.Animal.Animal;
import org.e2e.e2e.Animal.AnimalService;
import org.e2e.e2e.Vacuna.Vacuna;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VacunaService {

    private final VacunaRepository vacunaRepository;
    private final AnimalService animalService;

    public List<Vacuna> obtenerVacunasPorAnimal(Long animalId) {
        Animal animal = animalService.obtenerAnimalPorId(animalId);
        return animal.getVacunas();
    }

    public Vacuna guardarVacuna(Vacuna vacuna) {
        return vacunaRepository.save(vacuna);
    }

    public void eliminarVacuna(Long id) {
        vacunaRepository.deleteById(id);
    }
}
