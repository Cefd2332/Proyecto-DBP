package org.e2e.e2e.CitaVeterinaria;

import lombok.RequiredArgsConstructor;
import org.e2e.e2e.Animal.Animal;
import org.e2e.e2e.Animal.AnimalService;
import org.e2e.e2e.Email.EmailEvent;
import org.e2e.e2e.Notificacion.NotificacionPushService;
import org.e2e.e2e.Usuario.Usuario;
import org.e2e.e2e.exceptions.NotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CitaVeterinariaService {

    private final CitaVeterinariaRepository citaVeterinariaRepository;
    private final AnimalService animalService;
    private final ApplicationEventPublisher eventPublisher;
    private final NotificacionPushService notificacionPushService;  // Inyectamos el servicio de notificaciones push

    public List<CitaVeterinaria> obtenerCitasPorAnimal(Long animalId) {
        Animal animal = animalService.obtenerAnimalPorId(animalId);
        return animal.getCitasVeterinarias();
    }

    public CitaVeterinaria guardarCita(CitaVeterinariaRequestDto citaDto) {
        Animal animal = animalService.obtenerAnimalPorId(citaDto.getAnimalId());

        CitaVeterinaria cita = new CitaVeterinaria();
        cita.setFechaCita(citaDto.getFechaCita());
        cita.setVeterinario(citaDto.getVeterinario());
        // Estado por defecto si no se especifica en la solicitud
        cita.setEstado(citaDto.getEstado() != null ? citaDto.getEstado() : EstadoCita.PENDIENTE);
        cita.setAnimal(animal);

        // Guardar la cita en la base de datos
        CitaVeterinaria citaGuardada = citaVeterinariaRepository.save(cita);

        // Obtener el adoptante (dueño) del animal
        Usuario adoptante = animal.getAdoptante();
        String emailSubject = "Nueva cita veterinaria para " + animal.getNombre();
        String emailBody = "Estimado " + adoptante.getNombre() + ",\n\n" +
                "Se ha registrado una nueva cita para su mascota " + animal.getNombre() + ".\n" +
                "Fecha de la cita: " + cita.getFechaCita() + "\n" +
                "Veterinario: " + cita.getVeterinario() + "\n\n" +
                "Saludos,\n" +
                "Equipo de Adopción y Seguimiento de Animales";

        // Disparar el evento de correo electrónico
        eventPublisher.publishEvent(new EmailEvent(adoptante.getEmail(), emailSubject, emailBody));

        // Enviar notificación push al adoptante
        if (adoptante.getToken() != null && !adoptante.getToken().isEmpty()) {
            String pushTitle = "Nueva cita para " + animal.getNombre();
            String pushBody = "Se ha registrado una nueva cita con el veterinario " + cita.getVeterinario() +
                    " para tu mascota " + animal.getNombre() + ". Fecha: " + cita.getFechaCita();
            notificacionPushService.enviarNotificacion(adoptante.getToken(), pushTitle, pushBody);
        }

        return citaGuardada;
    }

    public void eliminarCita(Long id) {
        if (!citaVeterinariaRepository.existsById(id)) {
            throw new NotFoundException("Cita veterinaria no encontrada");
        }
        citaVeterinariaRepository.deleteById(id);
    }

    public CitaVeterinariaResponseDto convertirCitaAResponseDto(CitaVeterinaria cita) {
        CitaVeterinariaResponseDto responseDto = new CitaVeterinariaResponseDto();
        responseDto.setId(cita.getId());
        responseDto.setFechaCita(cita.getFechaCita());
        responseDto.setVeterinario(cita.getVeterinario());
        responseDto.setAnimalId(cita.getAnimal().getId());
        responseDto.setEstado(cita.getEstado());  // Agregamos el estado en el DTO de respuesta
        return responseDto;
    }
}
