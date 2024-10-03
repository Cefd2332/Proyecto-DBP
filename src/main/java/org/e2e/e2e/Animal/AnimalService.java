package org.e2e.e2e.Animal;

import lombok.RequiredArgsConstructor;
import org.e2e.e2e.Usuario.Usuario;
import org.e2e.e2e.Usuario.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final UsuarioRepository usuarioRepository;

    public List<Animal> obtenerTodosLosAnimales() {
        return animalRepository.findAll();
    }

    public Animal guardarAnimal(AnimalRequestDto animalDto) {
        Usuario adoptante = usuarioRepository.findById(animalDto.getAdoptanteId())
                .orElseThrow(() -> new RuntimeException("Adoptante no encontrado"));

        Animal animal = new Animal();
        animal.setNombre(animalDto.getNombre());
        animal.setEspecie(animalDto.getEspecie());
        animal.setEdad(animalDto.getEdad());
        animal.setEstadoSalud(animalDto.getEstadoSalud());
        animal.setAdoptante(adoptante);

        return animalRepository.save(animal);
    }

    public Animal obtenerAnimalPorId(Long id) {
        return animalRepository.findById(id).orElseThrow(() -> new RuntimeException("Animal no encontrado"));
    }

    public Animal actualizarAnimal(Long id, AnimalRequestDto animalDto) {
        Animal animal = obtenerAnimalPorId(id);
        animal.setNombre(animalDto.getNombre());
        animal.setEspecie(animalDto.getEspecie());
        animal.setEdad(animalDto.getEdad());
        animal.setEstadoSalud(animalDto.getEstadoSalud());

        Usuario adoptante = usuarioRepository.findById(animalDto.getAdoptanteId())
                .orElseThrow(() -> new RuntimeException("Adoptante no encontrado"));
        animal.setAdoptante(adoptante);

        return animalRepository.save(animal);
    }

    public void eliminarAnimal(Long id) {
        animalRepository.deleteById(id);
    }

    public AnimalResponseDto convertirAnimalAResponseDto(Animal animal) {
        AnimalResponseDto responseDto = new AnimalResponseDto();
        responseDto.setId(animal.getId());
        responseDto.setNombre(animal.getNombre());
        responseDto.setEspecie(animal.getEspecie());
        responseDto.setEdad(animal.getEdad());
        responseDto.setEstadoSalud(animal.getEstadoSalud());
        responseDto.setAdoptanteId(animal.getAdoptante().getId());
        return responseDto;
    }
}
