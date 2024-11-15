package org.e2e.e2e.CitaVeterinaria;

import org.e2e.e2e.Adoptante.Adoptante;
import org.e2e.e2e.Animal.Animal;
import org.e2e.e2e.Animal.AnimalService;
import org.e2e.e2e.Email.EmailEvent;
import org.e2e.e2e.Notificacion.NotificacionPushService;
import org.e2e.e2e.exceptions.NotFoundException;
import org.e2e.e2e.exceptions.ConflictException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CitaVeterinariaService {

    private static final Logger logger = LoggerFactory.getLogger(CitaVeterinariaService.class);

    private final CitaVeterinariaRepository citaVeterinariaRepository;
    private final AnimalService animalService;
    private final ApplicationEventPublisher eventPublisher;
    private final NotificacionPushService notificacionPushService;

    /**
     * Constructor que inyecta todas las dependencias necesarias.
     *
     * @param citaVeterinariaRepository Repositorio para manejar citas veterinarias.
     * @param animalService            Servicio para manejar operaciones relacionadas con animales.
     * @param eventPublisher           Publicador de eventos para enviar correos electrónicos.
     * @param notificacionPushService  Servicio para enviar notificaciones push.
     */
    public CitaVeterinariaService(CitaVeterinariaRepository citaVeterinariaRepository,
                                  AnimalService animalService,
                                  ApplicationEventPublisher eventPublisher,
                                  NotificacionPushService notificacionPushService) {
        this.citaVeterinariaRepository = citaVeterinariaRepository;
        this.animalService = animalService;
        this.eventPublisher = eventPublisher;
        this.notificacionPushService = notificacionPushService;
    }

    /**
     * Obtener citas por animal.
     *
     * @param animalId ID del animal.
     * @return Lista de citas veterinarias.
     */
    public List<CitaVeterinaria> obtenerCitasPorAnimal(Long animalId) {
        Animal animal = animalService.obtenerAnimalPorId(animalId);
        return animal.getCitasVeterinarias();
    }

    /**
     * Guardar una nueva cita veterinaria.
     *
     * @param citaDto DTO con los datos de la cita veterinaria.
     * @return Cita veterinaria guardada.
     */
    public CitaVeterinaria guardarCita(CitaVeterinariaRequestDto citaDto) {
        Animal animal = animalService.obtenerAnimalPorId(citaDto.getAnimalId());

        if (citaVeterinariaRepository.existsByFechaCitaAndVeterinario(citaDto.getFechaCita(), citaDto.getVeterinario())) {
            throw new ConflictException("Ya existe una cita con el mismo veterinario en esa fecha y hora.");
        }

        CitaVeterinaria cita = new CitaVeterinaria();
        cita.setFechaCita(citaDto.getFechaCita());
        cita.setVeterinario(citaDto.getVeterinario());
        cita.setEstado(citaDto.getEstado() != null ? citaDto.getEstado() : EstadoCita.PENDIENTE);
        cita.setAnimal(animal);

        CitaVeterinaria citaGuardada = citaVeterinariaRepository.save(cita);
        enviarNotificacionesAsync(citaGuardada, "Nueva cita veterinaria para " + animal.getNombre());

        return citaGuardada;
    }

    /**
     * Eliminar una cita veterinaria por su ID.
     *
     * @param id ID de la cita veterinaria a eliminar.
     */
    public void eliminarCita(Long id) {
        CitaVeterinaria cita = citaVeterinariaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cita veterinaria no encontrada con ID: " + id));

        Animal animal = cita.getAnimal();
        citaVeterinariaRepository.deleteById(id);
        enviarNotificacionesAsync(cita, "Cita veterinaria eliminada para " + animal.getNombre());
    }

    /**
     * Actualizar el estado de una cita veterinaria.
     *
     * @param id          ID de la cita veterinaria.
     * @param nuevoEstado Nuevo estado de la cita.
     * @return Cita veterinaria actualizada.
     */
    public CitaVeterinaria actualizarEstadoCita(Long id, EstadoCita nuevoEstado) {
        CitaVeterinaria cita = citaVeterinariaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cita veterinaria no encontrada con ID: " + id));

        cita.setEstado(nuevoEstado);
        CitaVeterinaria citaActualizada = citaVeterinariaRepository.save(cita);
        enviarNotificacionesAsync(citaActualizada, "Estado de cita actualizado para " + cita.getAnimal().getNombre());

        return citaActualizada;
    }

    /**
     * Convertir una cita veterinaria a DTO de respuesta.
     *
     * @param cita Cita veterinaria a convertir.
     * @return DTO de respuesta.
     */
    public CitaVeterinariaResponseDto convertirCitaAResponseDto(CitaVeterinaria cita) {
        CitaVeterinariaResponseDto responseDto = new CitaVeterinariaResponseDto();
        responseDto.setId(cita.getId());
        responseDto.setFechaCita(cita.getFechaCita());
        responseDto.setVeterinario(cita.getVeterinario());
        responseDto.setAnimalId(cita.getAnimal().getId());
        responseDto.setEstado(cita.getEstado());
        return responseDto;
    }

    /**
     * Método auxiliar para enviar correos y notificaciones de forma asíncrona.
     *
     * @param cita    Cita veterinaria asociada.
     * @param subject Asunto de la notificación.
     */
    @Async
    public void enviarNotificacionesAsync(CitaVeterinaria cita, String subject) {
        Animal animal = cita.getAnimal();
        Adoptante adoptante = animal.getAdoptante();

        String emailSubject = subject;
        String emailBody = "Estimado " + adoptante.getNombre() + ",\n\n" +
                "Detalles de la cita para su mascota " + animal.getNombre() + ":\n" +
                "Fecha de la cita: " + cita.getFechaCita() + "\n" +
                "Veterinario: " + cita.getVeterinario() + "\n" +
                "Estado: " + cita.getEstado() + "\n\n" +
                "Saludos,\n" +
                "Equipo de Adopción y Seguimiento de Animales";

        // Publicar evento de envío de email
        eventPublisher.publishEvent(new EmailEvent(adoptante.getEmail(), emailSubject, emailBody));

        // Enviar notificación push al adoptante si tiene un token válido
        if (adoptante.getDeviceToken() != null && !adoptante.getDeviceToken().isEmpty()) {
            String pushTitle = subject;
            String pushBody = "Fecha de la cita: " + cita.getFechaCita() +
                    ". Veterinario: " + cita.getVeterinario() + ". Estado: " + cita.getEstado();
            notificacionPushService.enviarNotificacion(adoptante, pushTitle, pushBody);
        } else {
            logger.warn("No se pudo enviar notificación push a Adoptante ID {}: No hay un token de dispositivo registrado.", adoptante.getId());
        }
    }
}
