package org.e2e.e2e.Adopcion;

import org.e2e.e2e.Adoptante.Adoptante;
import org.e2e.e2e.Adoptante.AdoptanteService;
import org.e2e.e2e.Animal.Animal;
import org.e2e.e2e.Animal.AnimalService;
import org.e2e.e2e.Email.EmailEvent;
import org.e2e.e2e.Notificacion.NotificacionPushService;
import org.e2e.e2e.exceptions.ConflictException;
import org.e2e.e2e.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdopcionService {

    private static final Logger logger = LoggerFactory.getLogger(AdopcionService.class);

    private final AdopcionRepository adopcionRepository;
    private final AdoptanteService adoptanteService;
    private final AnimalService animalService;
    private final ApplicationEventPublisher eventPublisher;
    private final NotificacionPushService notificacionPushService;

    /**
     * Constructor que inyecta todas las dependencias necesarias.
     *
     * @param adopcionRepository     Repositorio para manejar adopciones.
     * @param adoptanteService       Servicio para manejar operaciones relacionadas con adoptantes.
     * @param animalService          Servicio para manejar operaciones relacionadas con animales.
     * @param eventPublisher         Publicador de eventos para enviar correos electrónicos.
     * @param notificacionPushService Servicio para enviar notificaciones push.
     */
    public AdopcionService(AdopcionRepository adopcionRepository,
                           AdoptanteService adoptanteService,
                           AnimalService animalService,
                           ApplicationEventPublisher eventPublisher,
                           NotificacionPushService notificacionPushService) {
        this.adopcionRepository = adopcionRepository;
        this.adoptanteService = adoptanteService;
        this.animalService = animalService;
        this.eventPublisher = eventPublisher;
        this.notificacionPushService = notificacionPushService;
    }

    /**
     * Obtener todas las adopciones.
     *
     * @return Lista de adopciones.
     */
    public List<Adopcion> obtenerTodasLasAdopciones() {
        return adopcionRepository.findAll();
    }

    /**
     * Registrar una nueva adopción.
     *
     * @param adopcionDto DTO con los datos de la adopción.
     * @return Adopción registrada.
     */
    public Adopcion registrarAdopcion(AdopcionRequestDto adopcionDto) {
        Adoptante adoptante = adoptanteService.obtenerAdoptantePorId(adopcionDto.getAdoptanteId());
        Animal animal = animalService.obtenerAnimalPorId(adopcionDto.getAnimalId());

        // Verificar si el animal ya ha sido adoptado
        if (adopcionRepository.existsByAnimalId(animal.getId())) {
            throw new ConflictException("Este animal ya ha sido adoptado.");  // Excepción de conflicto
        }

        // Registrar la adopción
        Adopcion adopcion = new Adopcion();
        adopcion.setFechaAdopcion(adopcionDto.getFechaAdopcion());
        adopcion.setAdoptante(adoptante);
        adopcion.setAnimal(animal);

        Adopcion adopcionGuardada = adopcionRepository.save(adopcion);

        // Enviar correo de confirmación y notificaciones
        enviarNotificacionesAdopcion(adoptante, animal, adopcionGuardada);

        return adopcionGuardada;
    }

    /**
     * Obtener una adopción por su ID.
     *
     * @param id ID de la adopción.
     * @return Adopción encontrada.
     */
    public Adopcion obtenerAdopcionPorId(Long id) {
        return adopcionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Adopción no encontrada con ID: " + id));  // Excepción si no se encuentra la adopción
    }

    /**
     * Eliminar una adopción por su ID.
     *
     * @param id ID de la adopción a eliminar.
     */
    public void eliminarAdopcion(Long id) {
        if (!adopcionRepository.existsById(id)) {
            throw new NotFoundException("Adopción no encontrada con ID: " + id);  // Excepción si no se encuentra la adopción
        }
        adopcionRepository.deleteById(id);
        logger.info("Adopción eliminada con ID: {}", id);
    }

    /**
     * Método para enviar notificaciones y correos electrónicos de adopción.
     *
     * @param adoptante Adoptante que realiza la adopción.
     * @param animal    Animal adoptado.
     * @param adopcion  Adopción registrada.
     */
    private void enviarNotificacionesAdopcion(Adoptante adoptante, Animal animal, Adopcion adopcion) {
        String emailSubject = "Confirmación de adopción de " + animal.getNombre();
        String emailBody = "Estimado " + adoptante.getNombre() + ",\n\n" +
                "Felicitaciones, ha adoptado a " + animal.getNombre() + " con éxito.\n" +
                "Fecha de adopción: " + adopcion.getFechaAdopcion() + "\n\n" +
                "Saludos,\n" +
                "Equipo de Adopción y Seguimiento de Animales";

        // Disparar el evento de correo electrónico
        eventPublisher.publishEvent(new EmailEvent(adoptante.getEmail(), emailSubject, emailBody));
        logger.info("Evento de correo electrónico publicado para Adoptante ID: {}", adoptante.getId());

        // Enviar notificación push si el adoptante tiene un token válido
        if (adoptante.getDeviceToken() != null && !adoptante.getDeviceToken().isEmpty()) {
            String pushTitle = "Adopción exitosa de " + animal.getNombre();
            String pushBody = "Felicitaciones, ha adoptado a " + animal.getNombre() + " con éxito. ¡Gracias por brindar un hogar!";
            notificacionPushService.enviarNotificacion(adoptante, pushTitle, pushBody);
            logger.info("Notificación push enviada a Adoptante ID: {}", adoptante.getId());
        } else {
            logger.warn("No se pudo enviar notificación push a Adoptante ID {}: No hay un token de dispositivo registrado.", adoptante.getId());
        }
    }

    /**
     * Convertir una entidad Adopcion a DTO de respuesta.
     *
     * @param adopcion Adopción a convertir.
     * @return DTO de respuesta.
     */
    public AdopcionResponseDto convertirAdopcionAResponseDto(Adopcion adopcion) {
        AdopcionResponseDto responseDto = new AdopcionResponseDto();
        responseDto.setId(adopcion.getId());
        responseDto.setFechaAdopcion(adopcion.getFechaAdopcion());
        responseDto.setAdoptanteId(adopcion.getAdoptante().getId());
        responseDto.setAnimalId(adopcion.getAnimal().getId());
        return responseDto;
    }
}
