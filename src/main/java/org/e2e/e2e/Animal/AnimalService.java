package org.e2e.e2e.Animal;

import org.e2e.e2e.Adoptante.Adoptante;
import org.e2e.e2e.Adoptante.AdoptanteRepository;
import org.e2e.e2e.Email.EmailEvent;
import org.e2e.e2e.Notificacion.NotificacionPushService;
import org.e2e.e2e.exceptions.NotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final AdoptanteRepository adoptanteRepository;
    private final RegistroEstadoAnimalRepository registroEstadoAnimalRepository;
    private final NotificacionPushService notificacionPushService;
    private final ApplicationEventPublisher eventPublisher;

    // Constructor que inyecta todas las dependencias
    public AnimalService(AnimalRepository animalRepository,
                         AdoptanteRepository adoptanteRepository,
                         RegistroEstadoAnimalRepository registroEstadoAnimalRepository,
                         NotificacionPushService notificacionPushService,
                         ApplicationEventPublisher eventPublisher) {
        this.animalRepository = animalRepository;
        this.adoptanteRepository = adoptanteRepository;
        this.registroEstadoAnimalRepository = registroEstadoAnimalRepository;
        this.notificacionPushService = notificacionPushService;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Obtener todos los animales.
     *
     * @return Lista de todos los animales.
     */
    public List<Animal> obtenerTodosLosAnimales() {
        return animalRepository.findAll();
    }

    /**
     * Guardar un nuevo animal.
     *
     * @param animalDto DTO con la información del animal.
     * @return Animal guardado.
     */
    public Animal guardarAnimal(AnimalRequestDto animalDto) {
        Adoptante adoptante = adoptanteRepository.findById(animalDto.getAdoptanteId())
                .orElseThrow(() -> new NotFoundException("Adoptante no encontrado con ID: " + animalDto.getAdoptanteId()));

        // Crear un nuevo objeto Animal y establecer sus atributos
        Animal animal = new Animal();
        animal.setNombre(animalDto.getNombre());
        animal.setEspecie(animalDto.getEspecie());
        animal.setEdad(animalDto.getEdad());
        animal.setUnidadEdad(animalDto.getUnidadEdad()); // Nuevo campo
        animal.setEstadoSalud(animalDto.getEstadoSalud());
        animal.setFechaAdopcion(animalDto.getFechaAdopcion());
        animal.setEstadoActual(EstadoAnimal.SANO);  // Estado inicial del animal
        animal.setAdoptante(adoptante);

        Animal animalGuardado = animalRepository.save(animal);

        // Enviar notificaciones de creación
        enviarNotificaciones(animalGuardado, "Nuevo animal registrado: " + animalGuardado.getNombre());

        return animalGuardado;
    }

    /**
     * Obtener un animal por su ID.
     *
     * @param id ID del animal.
     * @return Animal encontrado.
     */
    public Animal obtenerAnimalPorId(Long id) {
        return animalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Animal no encontrado con ID: " + id));
    }

    /**
     * Actualizar la información de un animal existente.
     *
     * @param id        ID del animal a actualizar.
     * @param animalDto DTO con la nueva información del animal.
     * @return Animal actualizado.
     */
    public Animal actualizarAnimal(Long id, AnimalRequestDto animalDto) {
        Animal animal = obtenerAnimalPorId(id);

        animal.setNombre(animalDto.getNombre());
        animal.setEspecie(animalDto.getEspecie());
        animal.setEdad(animalDto.getEdad());
        animal.setUnidadEdad(animalDto.getUnidadEdad()); // Nuevo campo
        animal.setEstadoSalud(animalDto.getEstadoSalud());
        animal.setFechaAdopcion(animalDto.getFechaAdopcion());

        Adoptante adoptante = adoptanteRepository.findById(animalDto.getAdoptanteId())
                .orElseThrow(() -> new NotFoundException("Adoptante no encontrado con ID: " + animalDto.getAdoptanteId()));
        animal.setAdoptante(adoptante);

        // Actualizar el estado del animal si se proporciona en el DTO
        if (animalDto.getEstadoActual() != null) {
            actualizarEstadoAnimal(animal, animalDto.getEstadoActual());
        }

        Animal animalActualizado = animalRepository.save(animal);

        // Enviar notificaciones de actualización
        enviarNotificaciones(animalActualizado, "Información del animal actualizada: " + animalActualizado.getNombre());

        return animalActualizado;
    }

    /**
     * Eliminar un animal por su ID.
     *
     * @param id ID del animal a eliminar.
     */
    public void eliminarAnimal(Long id) {
        Animal animal = obtenerAnimalPorId(id);

        animalRepository.delete(animal);

        // Enviar notificación de eliminación
        enviarNotificaciones(animal, "Animal eliminado: " + animal.getNombre());
    }

    /**
     * Actualizar el estado de salud de un animal y registrar el cambio en el historial.
     *
     * @param animalId    ID del animal.
     * @param nuevoEstado Nuevo estado de salud.
     * @return Animal con el estado actualizado.
     */
    public Animal actualizarEstadoAnimal(Long animalId, EstadoAnimal nuevoEstado) {
        Animal animal = obtenerAnimalPorId(animalId);
        return actualizarEstadoAnimal(animal, nuevoEstado);
    }

    /**
     * Método privado para actualizar el estado y registrar el cambio.
     *
     * @param animal      Animal a actualizar.
     * @param nuevoEstado Nuevo estado de salud.
     * @return Animal con el estado actualizado.
     */
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

    /**
     * Convertir una entidad Animal a un DTO de respuesta.
     *
     * @param animal Animal a convertir.
     * @return DTO de respuesta.
     */
    public AnimalResponseDto convertirAnimalAResponseDto(Animal animal) {
        AnimalResponseDto responseDto = new AnimalResponseDto();
        responseDto.setId(animal.getId());
        responseDto.setNombre(animal.getNombre());
        responseDto.setEspecie(animal.getEspecie());
        responseDto.setEdad(animal.getEdad());
        responseDto.setUnidadEdad(animal.getUnidadEdad());
        responseDto.setEstadoSalud(animal.getEstadoSalud());
        responseDto.setEstadoActual(animal.getEstadoActual());  // Estado actual del animal
        responseDto.setAdoptanteId(animal.getAdoptante().getId());
        return responseDto;
    }

    /**
     * Obtener el historial de cambios de estado de un animal.
     *
     * @param animalId ID del animal.
     * @return Lista de registros de cambios de estado.
     */
    public List<RegistroEstadoAnimalResponseDto> obtenerHistorialEstados(Long animalId) {
        Animal animal = obtenerAnimalPorId(animalId);
        return animal.getRegistroEstadoAnimal().stream()
                .map(this::convertirRegistroEstadoAResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Obtener todos los animales adoptados por un adoptante específico.
     *
     * @param adoptanteId ID del adoptante.
     * @return Lista de animales adoptados por el adoptante.
     */
    public List<Animal> obtenerAnimalesPorAdoptanteId(Long adoptanteId) {
        // Verificar si el adoptante existe
        if (!adoptanteRepository.existsById(adoptanteId)) {
            throw new NotFoundException("Adoptante no encontrado con ID: " + adoptanteId);
        }

        // Utilizar el método del repositorio para obtener animales por adoptanteId
        return animalRepository.findByAdoptanteId(adoptanteId);
    }

    /**
     * Convertir una entidad RegistroEstadoAnimal a un DTO de respuesta.
     *
     * @param registroEstado Registro de cambio de estado.
     * @return DTO de respuesta.
     */
    private RegistroEstadoAnimalResponseDto convertirRegistroEstadoAResponseDto(RegistroEstadoAnimal registroEstado) {
        RegistroEstadoAnimalResponseDto responseDto = new RegistroEstadoAnimalResponseDto();
        responseDto.setId(registroEstado.getId());
        responseDto.setEstado(registroEstado.getEstado());
        responseDto.setFechaCambio(registroEstado.getFechaCambio());
        responseDto.setAnimalId(registroEstado.getAnimal().getId());
        return responseDto;
    }

    /**
     * Método auxiliar para enviar correos electrónicos y notificaciones push.
     *
     * @param animal  Animal relacionado con la notificación.
     * @param subject Asunto de la notificación.
     */
    private void enviarNotificaciones(Animal animal, String subject) {
        Adoptante adoptante = animal.getAdoptante();

        // Enviar correo electrónico
        String emailSubject = subject;
        String emailBody = "Estimado " + adoptante.getNombre() + ",\n\n" +
                "Detalles del animal " + animal.getNombre() + ":\n" +
                "Especie: " + animal.getEspecie() + "\n" +
                "Edad: " + animal.getEdad() + " " + animal.getUnidadEdad() + "\n" +
                "Estado de salud: " + animal.getEstadoSalud() + "\n" +
                "Estado actual: " + animal.getEstadoActual() + "\n\n" +
                "Saludos,\n" +
                "Equipo de Adopción y Seguimiento de Animales";

        // Publicar evento de envío de email
        eventPublisher.publishEvent(new EmailEvent(adoptante.getEmail(), emailSubject, emailBody));

        // Enviar notificación push si es necesario
        notificacionPushService.enviarNotificacion(adoptante, emailSubject, emailBody);
    }
}
