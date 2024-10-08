package org.e2e.e2e.CitaVeterinaria;

import lombok.RequiredArgsConstructor;
import org.e2e.e2e.Animal.Animal;
import org.e2e.e2e.Animal.AnimalService;
import org.e2e.e2e.Email.EmailEvent;
import org.e2e.e2e.Notificacion.NotificacionPushService;
import org.e2e.e2e.Usuario.Usuario;
import org.e2e.e2e.exceptions.NotFoundException;
import org.e2e.e2e.exceptions.ConflictException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CitaVeterinariaService {

    private final CitaVeterinariaRepository citaVeterinariaRepository;
    private final AnimalService animalService;
    private final ApplicationEventPublisher eventPublisher;
    private final NotificacionPushService notificacionPushService;

    // Obtener citas por animal
    public List<CitaVeterinaria> obtenerCitasPorAnimal(Long animalId) {
        Animal animal = animalService.obtenerAnimalPorId(animalId);
        return animal.getCitasVeterinarias();
    }

    // Guardar una nueva cita
    public CitaVeterinaria guardarCita(CitaVeterinariaRequestDto citaDto) {
        Animal animal = animalService.obtenerAnimalPorId(citaDto.getAnimalId());

        // Verificar si ya existe una cita con el mismo veterinario en la misma fecha y hora para evitar conflictos
        if (citaVeterinariaRepository.existsByFechaCitaAndVeterinario(citaDto.getFechaCita(), citaDto.getVeterinario())) {
            throw new ConflictException("Ya existe una cita con el mismo veterinario en esa fecha y hora.");
        }

        CitaVeterinaria cita = new CitaVeterinaria();
        cita.setFechaCita(citaDto.getFechaCita());
        cita.setVeterinario(citaDto.getVeterinario());
        cita.setEstado(citaDto.getEstado() != null ? citaDto.getEstado() : EstadoCita.PENDIENTE);
        cita.setAnimal(animal);

        // Guardar la cita en la base de datos
        CitaVeterinaria citaGuardada = citaVeterinariaRepository.save(cita);

        // Enviar notificación de creación (Asíncrono)
        enviarNotificacionesAsync(citaGuardada, "Nueva cita veterinaria para " + animal.getNombre());

        return citaGuardada;
    }

    // Eliminar una cita
    public void eliminarCita(Long id) {
        CitaVeterinaria cita = citaVeterinariaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cita veterinaria no encontrada"));

        // Obtener el animal relacionado a la cita antes de eliminarla
        Animal animal = cita.getAnimal();

        // Eliminar la cita
        citaVeterinariaRepository.deleteById(id);

        // Enviar notificación de eliminación (Asíncrono)
        enviarNotificacionesAsync(cita, "Cita veterinaria eliminada para " + animal.getNombre());
    }

    // Actualizar el estado de una cita
    public CitaVeterinaria actualizarEstadoCita(Long id, EstadoCita nuevoEstado) {
        CitaVeterinaria cita = citaVeterinariaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cita no encontrada"));

        // Actualizar el estado de la cita
        cita.setEstado(nuevoEstado);
        CitaVeterinaria citaActualizada = citaVeterinariaRepository.save(cita);

        // Enviar notificación de actualización de estado (Asíncrono)
        enviarNotificacionesAsync(citaActualizada, "Estado de cita actualizado para " + cita.getAnimal().getNombre());

        return citaActualizada;
    }

    // Convertir una cita a DTO de respuesta
    public CitaVeterinariaResponseDto convertirCitaAResponseDto(CitaVeterinaria cita) {
        CitaVeterinariaResponseDto responseDto = new CitaVeterinariaResponseDto();
        responseDto.setId(cita.getId());
        responseDto.setFechaCita(cita.getFechaCita());
        responseDto.setVeterinario(cita.getVeterinario());
        responseDto.setAnimalId(cita.getAnimal().getId());
        responseDto.setEstado(cita.getEstado());
        return responseDto;
    }

    // Método auxiliar para enviar correos y notificaciones de forma asíncrona
    @Async
    public void enviarNotificacionesAsync(CitaVeterinaria cita, String subject) {
        Animal animal = cita.getAnimal();
        Usuario adoptante = animal.getAdoptante();

        // Enviar correo electrónico
        String emailSubject = subject;
        String emailBody = "Estimado " + adoptante.getNombre() + ",\n\n" +
                "Detalles de la cita para su mascota " + animal.getNombre() + ":\n" +
                "Fecha de la cita: " + cita.getFechaCita() + "\n" +
                "Veterinario: " + cita.getVeterinario() + "\n" +
                "Estado: " + cita.getEstado() + "\n\n" +
                "Saludos,\n" +
                "Equipo de Adopción y Seguimiento de Animales";

        eventPublisher.publishEvent(new EmailEvent(adoptante.getEmail(), emailSubject, emailBody));

        // Enviar notificación push al adoptante si tiene un token válido
        if (adoptante.getToken() != null && !adoptante.getToken().isEmpty()) {
            String pushTitle = subject;
            String pushBody = "Fecha de la cita: " + cita.getFechaCita() +
                    ". Veterinario: " + cita.getVeterinario() + ". Estado: " + cita.getEstado();
            notificacionPushService.enviarNotificacion(adoptante.getToken(), pushTitle, pushBody);
        }
    }
}
