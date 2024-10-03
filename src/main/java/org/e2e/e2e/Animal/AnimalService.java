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
    private final RegistroEstadoAnimalRepository registroEstadoAnimalRepository;

    // Obtener todos los animales
    public List<Animal> obtenerTodosLosAnimales() {
        return animalRepository.findAll();
    }

    // Guardar un nuevo animal
    public Animal guardarAnimal(AnimalRequestDto animalDto) {
        Usuario adoptante = usuarioRepository.findById(animalDto.getAdoptanteId())
                .orElseThrow(() -> new RuntimeException("Adoptante no encontrado"));

        Animal animal = new Animal();
        animal.setNombre(animalDto.getNombre());
        animal.setEspecie(animalDto.getEspecie());
        animal.setEdad(animalDto.getEdad());
        animal.setEstadoSalud(animalDto.getEstadoSalud());
        animal.setEstadoActual(EstadoAnimal.SANO);  // Estado inicial del animal
        animal.setAdoptante(adoptante);

        return animalRepository.save(animal);
    }

    // Obtener un animal por ID
    public Animal obtenerAnimalPorId(Long id) {
        return animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal no encontrado"));
    }

    // Actualizar la información del animal
    public Animal actualizarAnimal(Long id, AnimalRequestDto animalDto) {
        Animal animal = obtenerAnimalPorId(id);
        animal.setNombre(animalDto.getNombre());
        animal.setEspecie(animalDto.getEspecie());
        animal.setEdad(animalDto.getEdad());
        animal.setEstadoSalud(animalDto.getEstadoSalud());

        Usuario adoptante = usuarioRepository.findById(animalDto.getAdoptanteId())
                .orElseThrow(() -> new RuntimeException("Adoptante no encontrado"));
        animal.setAdoptante(adoptante);

        // Actualizar el estado del animal si se proporciona en el DTO
        if (animalDto.getEstado() != null) {
            actualizarEstadoAnimal(animal, animalDto.getEstado());
        }

        return animalRepository.save(animal);
    }

    // Eliminar un animal
    public void eliminarAnimal(Long id) {
        animalRepository.deleteById(id);
    }

    // Actualizar el estado del animal y registrar el cambio en el historial
    public Animal actualizarEstadoAnimal(Long animalId, EstadoAnimal nuevoEstado) {
        Animal animal = obtenerAnimalPorId(animalId);
        return actualizarEstadoAnimal(animal, nuevoEstado);
    }

    // Método privado para actualizar el estado y registrar el cambio
    private Animal actualizarEstadoAnimal(Animal animal, EstadoAnimal nuevoEstado) {
        animal.setEstadoActual(nuevoEstado);

        // Registrar el cambio de estado en el historial
        RegistroEstadoAnimal registroEstado = new RegistroEstadoAnimal();
        registroEstado.setAnimal(animal);
        registroEstado.setEstado(nuevoEstado);
        registroEstado.setFechaCambio(java.time.LocalDateTime.now());

        registroEstadoAnimalRepository.save(registroEstado);

        return animalRepository.save(animal);
    }

    // Conversión de Animal a AnimalResponseDto
    public AnimalResponseDto convertirAnimalAResponseDto(Animal animal) {
        AnimalResponseDto responseDto = new AnimalResponseDto();
        responseDto.setId(animal.getId());
        responseDto.setNombre(animal.getNombre());
        responseDto.setEspecie(animal.getEspecie());
        responseDto.setEdad(animal.getEdad());
        responseDto.setEstadoSalud(animal.getEstadoSalud());
        responseDto.setAdoptanteId(animal.getAdoptante().getId());
        responseDto.setEstadoActual(animal.getEstadoActual());  // Estado actual del animal
        return responseDto;
    }
}
