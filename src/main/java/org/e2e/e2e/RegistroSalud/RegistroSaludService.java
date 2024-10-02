package org.e2e.e2e.RegistroSalud;

import lombok.RequiredArgsConstructor;
import org.e2e.e2e.Animal.Animal;
import org.e2e.e2e.Animal.AnimalService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistroSaludService {

    private final RegistroSaludRepository registroSaludRepository;
    private final AnimalService animalService;

    public List<RegistroSalud> obtenerHistorialMedico(Long animalId) {
        Animal animal = animalService.obtenerAnimalPorId(animalId);
        return animal.getHistorialMedico();
    }

    public RegistroSalud guardarRegistroSalud(RegistroSalud registroSalud) {
        return registroSaludRepository.save(registroSalud);
    }

    public void eliminarRegistroSalud(Long id) {
        registroSaludRepository.deleteById(id);
    }
}
