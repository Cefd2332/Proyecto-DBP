package org.e2e.e2e.Vacuna;

import lombok.RequiredArgsConstructor;
import org.e2e.e2e.Animal.Animal;
import org.e2e.e2e.Animal.AnimalService;
import org.e2e.e2e.Email.EmailEvent;
import org.e2e.e2e.Notificacion.NotificacionPushService;
import org.e2e.e2e.Usuario.Usuario;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VacunaService {

    private final VacunaRepository vacunaRepository;
    private final AnimalService animalService;
    private final ApplicationEventPublisher eventPublisher;
    private final NotificacionPushService notificacionPushService; // Inyectamos el servicio de notificaciones push

    public List<Vacuna> obtenerVacunasPorAnimal(Long animalId) {
        Animal animal = animalService.obtenerAnimalPorId(animalId);
        return animal.getVacunas();
    }

    public Vacuna guardarVacuna(VacunaRequestDto vacunaDto) {
        Animal animal = animalService.obtenerAnimalPorId(vacunaDto.getAnimalId());

        Vacuna vacuna = new Vacuna();
        vacuna.setNombre(vacunaDto.getNombre());
        vacuna.setFechaAplicacion(vacunaDto.getFechaAplicacion());
        vacuna.setAnimal(animal);

        // Guardar la vacuna en la base de datos
        Vacuna vacunaGuardada = vacunaRepository.save(vacuna);

        // Obtener el adoptante (dueño) del animal
        Usuario adoptante = animal.getAdoptante();
        String emailSubject = "Nueva vacuna registrada para " + animal.getNombre();
        String emailBody = "Estimado " + adoptante.getNombre() + ",\n\n" +
                "Se ha registrado una nueva vacuna para su mascota " + animal.getNombre() + ".\n" +
                "Nombre de la vacuna: " + vacuna.getNombre() + "\n" +
                "Fecha de aplicación: " + vacuna.getFechaAplicacion() + "\n\n" +
                "Saludos,\n" +
                "Equipo de Adopción y Seguimiento de Animales";

        // Disparar el evento de correo electrónico
        eventPublisher.publishEvent(new EmailEvent(adoptante.getEmail(), emailSubject, emailBody));

        // Enviar notificación push al adoptante
        if (adoptante.getToken() != null && !adoptante.getToken().isEmpty()) {
            String pushTitle = "Nueva vacuna registrada para " + animal.getNombre();
            String pushBody = "Se ha registrado una nueva vacuna: " + vacuna.getNombre() +
                    " para tu mascota " + animal.getNombre() + ". Fecha: " + vacuna.getFechaAplicacion();
            notificacionPushService.enviarNotificacion(adoptante.getToken(), pushTitle, pushBody);
        }

        return vacunaGuardada;
    }

    public void eliminarVacuna(Long id) {
        vacunaRepository.deleteById(id);
    }

    public VacunaResponseDto convertirVacunaAResponseDto(Vacuna vacuna) {
        VacunaResponseDto responseDto = new VacunaResponseDto();
        responseDto.setId(vacuna.getId());
        responseDto.setNombre(vacuna.getNombre());
        responseDto.setFechaAplicacion(vacuna.getFechaAplicacion());
        responseDto.setAnimalId(vacuna.getAnimal().getId());
        return responseDto;
    }

    // Método programado para enviar recordatorios de vacunas
    @Scheduled(cron = "0 0 8 * * ?") // Cada día a las 8 AM
    public void enviarRecordatoriosVacunas() {
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusDays(7);

        // Buscar vacunas próximas
        List<Vacuna> vacunasProximas = vacunaRepository.findVacunasProximas(today, nextWeek);

        for (Vacuna vacuna : vacunasProximas) {
            Usuario adoptante = vacuna.getAnimal().getAdoptante();
            String emailSubject = "Recordatorio: Vacuna próxima para " + vacuna.getAnimal().getNombre();
            String emailBody = "Estimado " + adoptante.getNombre() + ",\n\n" +
                    "Recuerde que la vacuna " + vacuna.getNombre() + " de su mascota " + vacuna.getAnimal().getNombre() +
                    " está programada para el " + vacuna.getFechaAplicacion() + ".\n\nSaludos,\n" +
                    "Equipo de Adopción y Seguimiento de Animales";

            // Enviar correo electrónico
            eventPublisher.publishEvent(new EmailEvent(adoptante.getEmail(), emailSubject, emailBody));

            // Enviar notificación push
            if (adoptante.getToken() != null && !adoptante.getToken().isEmpty()) {
                String pushTitle = "Recordatorio de vacuna para " + vacuna.getAnimal().getNombre();
                String pushBody = "La vacuna " + vacuna.getNombre() + " está programada para " + vacuna.getFechaAplicacion();
                notificacionPushService.enviarNotificacion(adoptante.getToken(), pushTitle, pushBody);
            }
        }
    }
}
