package org.e2e.e2e.Animal;

import lombok.RequiredArgsConstructor;
import org.e2e.e2e.Email.EmailEvent;
import org.e2e.e2e.Notificacion.NotificacionPushService;
import org.e2e.e2e.Usuario.Usuario;
import org.e2e.e2e.Usuario.UsuarioRepository;
import org.e2e.e2e.exceptions.NotFoundException; // Excepción personalizada
import org.e2e.e2e.exceptions.ConflictException; // Excepción de conflicto si fuera necesaria
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
                .orElseThrow(() -> new NotFoundException("Adoptante no encontrado con ID: " + animalDto.getAdoptanteId()));

        // Crear un nuevo objeto Animal y establecer sus atributos
        Animal animal = new Animal();
        animal.setNombre(animalDto.getNombre());
        animal.setEspecie(animalDto.getEspecie());
        animal.setEdad(animalDto.getEdad());
        animal.setEstadoSalud(animalDto.getEstadoSalud());
        animal.setEstadoActual(EstadoAnimal.SANO);  // Estado inicial del animal
        animal.setAdoptante(adoptante);

        Animal animalGuardado = animalRepository.save(animal);

        // Enviar notificaciones de creación
        enviarNotificaciones(animalGuardado, "Nuevo animal registrado: " + animalGuardado.getNombre());

        return animalGuardado;
    }

    // Obtener un animal por ID
    public Animal obtenerAnimalPorId(Long id) {
        return animalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Animal no encontrado con ID: " + id));
    }

    // Actualizar la información del animal
    public Animal actualizarAnimal(Long id, AnimalRequestDto animalDto) {
        Animal animal = obtenerAnimalPorId(id);

        animal.setNombre(animalDto.getNombre());
        animal.setEspecie(animalDto.getEspecie());
        animal.setEdad(animalDto.getEdad());
        animal.setEstadoSalud(animalDto.getEstadoSalud());

        Usuario adoptante = usuarioRepository.findById(animalDto.getAdoptanteId())
                .orElseThrow(() -> new NotFoundException("Adoptante no encontrado con ID: " + animalDto.getAdoptanteId()));
        animal.setAdoptante(adoptante);

        // Actualizar el estado del animal si se proporciona en el DTO
        if (animalDto.getEstado() != null) {
            actualizarEstadoAnimal(animal, animalDto.getEstado());
        }

        Animal animalActualizado = animalRepository.save(animal);

        // Enviar notificaciones de actualización
        enviarNotificaciones(animalActualizado, "Información del animal actualizada: " + animalActualizado.getNombre());

        return animalActualizado;
    }

    // Eliminar un animal
    public void eliminarAnimal(Long id) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Animal no encontrado con ID: " + id));

        animalRepository.deleteById(id);

        // Enviar notificación de eliminación
        enviarNotificaciones(animal, "Animal eliminado: " + animal.getNombre());
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

        // Enviar notificación de cambio de estado
        enviarNotificaciones(animal, "Cambio de estado de salud para " + animal.getNombre());

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

    // Método auxiliar para enviar correos y notificaciones
    private void enviarNotificaciones(Animal animal, String subject) {
        Usuario adoptante = animal.getAdoptante();

        // Enviar correo electrónico
        String emailSubject = subject;
        String emailBody = "Estimado " + adoptante.getNombre() + ",\n\n" +
                "Detalles del animal " + animal.getNombre() + ":\n" +
                "Especie: " + animal.getEspecie() + "\n" +
                "Edad: " + animal.getEdad() + "\n" +
                "Estado de salud: " + animal.getEstadoSalud() + "\n" +
                "Estado actual: " + animal.getEstadoActual() + "\n\n" +
                "Saludos,\n" +
                "Equipo de Adopción y Seguimiento de Animales";

        eventPublisher.publishEvent(new EmailEvent(adoptante.getEmail(), emailSubject, emailBody));

        // Enviar notificación push si hay token disponible
        if (adoptante.getToken() != null && !adoptante.getToken().isEmpty()) {
            String pushTitle = subject;
            String pushBody = "Estado actual de tu mascota " + animal.getNombre() + ": " + animal.getEstadoActual();
            notificacionPushService.enviarNotificacion(adoptante.getToken(), pushTitle, pushBody);
        }
    }
}
