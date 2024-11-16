// src/main/java/org/e2e/e2e/CitaVeterinaria/CitaVeterinariaService.java

package org.e2e.e2e.CitaVeterinaria;

import org.e2e.e2e.Adoptante.Adoptante;
import org.e2e.e2e.Animal.Animal;
import org.e2e.e2e.Animal.AnimalService;
import org.e2e.e2e.Email.EmailEvent;
import org.e2e.e2e.Notificacion.NotificacionPushService;
import org.e2e.e2e.exceptions.NotFoundException;
import org.e2e.e2e.exceptions.BadRequestException;
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

    // Constructor con inyección de dependencias
    public CitaVeterinariaService(CitaVeterinariaRepository citaVeterinariaRepository,
                                  AnimalService animalService,
                                  ApplicationEventPublisher eventPublisher,
                                  NotificacionPushService notificacionPushService) {
        this.citaVeterinariaRepository = citaVeterinariaRepository;
        this.animalService = animalService;
        this.eventPublisher = eventPublisher;
        this.notificacionPushService = notificacionPushService;
    }

    // Método para obtener todas las citas
    public List<CitaVeterinaria> obtenerTodasLasCitas() {
        return citaVeterinariaRepository.findAll();
    }

    // Método para obtener una cita por ID
    public CitaVeterinaria obtenerCitaPorId(Long id) {
        return citaVeterinariaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cita veterinaria no encontrada con ID: " + id));
    }

    // Método para obtener citas por ID de animal
    public List<CitaVeterinaria> obtenerCitasPorAnimal(Long animalId) {
        Animal animal = animalService.obtenerAnimalPorId(animalId);
        return citaVeterinariaRepository.findByAnimal(animal);
    }

    // Método para guardar una nueva cita
    public CitaVeterinaria guardarCita(CitaVeterinariaRequestDto citaDto) {
        Animal animal = animalService.obtenerAnimalPorId(citaDto.getAnimalId());

        if (citaVeterinariaRepository.existsByFechaCitaAndVeterinario(citaDto.getFechaCita(), citaDto.getVeterinario())) {
            throw new ConflictException("Ya existe una cita con el mismo veterinario en esa fecha y hora.");
        }

        CitaVeterinaria cita = new CitaVeterinaria();
        cita.setFechaCita(citaDto.getFechaCita());
        cita.setMotivo(citaDto.getMotivo());
        cita.setVeterinario(citaDto.getVeterinario());
        cita.setEstado(citaDto.getEstado() != null ? citaDto.getEstado() : EstadoCita.PENDIENTE);
        cita.setAnimal(animal);

        CitaVeterinaria citaGuardada = citaVeterinariaRepository.save(cita);
        enviarNotificacionesAsync(citaGuardada, "Nueva cita veterinaria para " + animal.getNombre());

        return citaGuardada;
    }

    // Método para eliminar una cita
    public void eliminarCita(Long id) {
        CitaVeterinaria cita = citaVeterinariaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cita veterinaria no encontrada con ID: " + id));

        Animal animal = cita.getAnimal();
        citaVeterinariaRepository.deleteById(id);
        enviarNotificacionesAsync(cita, "Cita veterinaria eliminada para " + animal.getNombre());
    }

    // Método para actualizar el estado de una cita
    public CitaVeterinaria actualizarEstadoCita(Long id, EstadoCita nuevoEstado) {
        CitaVeterinaria cita = citaVeterinariaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cita veterinaria no encontrada con ID: " + id));

        cita.setEstado(nuevoEstado);
        CitaVeterinaria citaActualizada = citaVeterinariaRepository.save(cita);
        enviarNotificacionesAsync(citaActualizada, "Estado de cita actualizado para " + citaActualizada.getAnimal().getNombre());

        return citaActualizada;
    }

    // Método para actualizar una cita completa
    public CitaVeterinaria actualizarCita(Long id, CitaUpdateDto citaUpdateDto) {
        CitaVeterinaria cita = citaVeterinariaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cita veterinaria no encontrada con ID: " + id));

        // Actualizar los campos de la cita
        cita.setFechaCita(citaUpdateDto.getFechaCita());
        cita.setMotivo(citaUpdateDto.getMotivo());
        cita.setVeterinario(citaUpdateDto.getVeterinario());

        // Validar y actualizar el estado
        try {
            EstadoCita estadoActualizado = EstadoCita.valueOf(citaUpdateDto.getEstado());
            cita.setEstado(estadoActualizado);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Estado de cita inválido: " + citaUpdateDto.getEstado());
        }

        CitaVeterinaria citaActualizada = citaVeterinariaRepository.save(cita);
        enviarNotificacionesAsync(citaActualizada, "Cita actualizada para " + citaActualizada.getAnimal().getNombre());

        return citaActualizada;
    }

    // Método para enviar notificaciones de manera asíncrona
    @Async
    public void enviarNotificacionesAsync(CitaVeterinaria cita, String subject) {
        Animal animal = cita.getAnimal();
        Adoptante adoptante = animal.getAdoptante();

        String emailSubject = subject;
        String emailBody = "Estimado " + adoptante.getNombre() + ",\n\n" +
                "Detalles de la cita para su mascota " + animal.getNombre() + ":\n" +
                "Fecha de la cita: " + cita.getFechaCita() + "\n" +
                "Motivo: " + cita.getMotivo() + "\n" +
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
                    ". Motivo: " + cita.getMotivo() +
                    ". Veterinario: " + cita.getVeterinario() + ". Estado: " + cita.getEstado();
            notificacionPushService.enviarNotificacion(adoptante, pushTitle, pushBody);
        } else {
            logger.warn("No se pudo enviar notificación push al Adoptante ID {}: No hay un token de dispositivo registrado.", adoptante.getId());
        }
    }

    // Método para convertir una cita a Response DTO
    public CitaVeterinariaResponseDto convertirCitaAResponseDto(CitaVeterinaria cita) {
        CitaVeterinariaResponseDto dto = new CitaVeterinariaResponseDto();
        dto.setId(cita.getId());
        dto.setFechaCita(cita.getFechaCita());
        dto.setMotivo(cita.getMotivo());
        dto.setVeterinario(cita.getVeterinario());
        dto.setEstado(cita.getEstado());
        dto.setAnimalId(cita.getAnimal().getId());
        return dto;
    }
}
