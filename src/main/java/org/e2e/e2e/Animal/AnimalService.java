package org.e2e.e2e.Animal;

import lombok.RequiredArgsConstructor;
import org.e2e.e2e.Usuario.Usuario;
import org.e2e.e2e.Usuario.UsuarioRepository;
import org.e2e.e2e.Notificacion.NotificacionPushService;
import org.e2e.e2e.Email.EmailEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final UsuarioRepository usuarioRepository;
    private final RegistroEstadoAnimalRepository registroEstadoAnimalRepository;
    private final NotificacionPushService notificacionPushService;
    private final ApplicationEventPublisher eventPublisher;

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

        // Enviar notificación push al adoptante si hay token disponible
        String mensajeNotificacion = "El estado de salud de tu mascota " + animal.getNombre() + " ha cambiado a: " + nuevoEstado;
        if (animal.getAdoptante().getToken() != null) {
            notificacionPushService.enviarNotificacion(animal.getAdoptante().getToken(), "Cambio de estado de salud", mensajeNotificacion);
        } else {
            // Enviar notificación por correo electrónico si no hay token
            String emailSubject = "Cambio de estado de salud para " + animal.getNombre();
            String emailBody = "Estimado " + animal.getAdoptante().getNombre() + ",\n\n" +
                    "El estado de salud de tu mascota ha cambiado a: " + nuevoEstado + ".\n\n" +
                    "Saludos,\n" +
                    "Equipo de Adopción y Seguimiento de Animales";

            eventPublisher.publishEvent(new EmailEvent(animal.getAdoptante().getEmail(), emailSubject, emailBody));
        }

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

    // Obtener el historial de cambios de estado del animal
    public List<RegistroEstadoAnimalResponseDto> obtenerHistorialEstados(Long animalId) {
        Animal animal = obtenerAnimalPorId(animalId);
        return animal.getRegistroEstadoAnimal().stream()
                .map(this::convertirRegistroEstadoAResponseDto)
                .toList();
    }

    // Conversión de RegistroEstadoAnimal a DTO
    private RegistroEstadoAnimalResponseDto convertirRegistroEstadoAResponseDto(RegistroEstadoAnimal registroEstado) {
        RegistroEstadoAnimalResponseDto responseDto = new RegistroEstadoAnimalResponseDto();
        responseDto.setId(registroEstado.getId());
        responseDto.setEstado(registroEstado.getEstado());
        responseDto.setFechaCambio(registroEstado.getFechaCambio());
        responseDto.setAnimalId(registroEstado.getAnimal().getId());
        return responseDto;
    }
}
