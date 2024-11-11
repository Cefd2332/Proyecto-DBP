package org.e2e.e2e.Vacuna;

import org.e2e.e2e.Animal.Animal;
import org.e2e.e2e.Animal.AnimalService;
import org.e2e.e2e.Email.EmailEvent;
import org.e2e.e2e.Notificacion.NotificacionPushService;
import org.e2e.e2e.Usuario.Usuario;
import org.e2e.e2e.exceptions.NotFoundException;
import org.e2e.e2e.exceptions.BadRequestException;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class VacunaService {

    private final VacunaRepository vacunaRepository;
    private final AnimalService animalService;
    private final ApplicationEventPublisher eventPublisher;
    private final NotificacionPushService notificacionPushService;

    // Constructor para inyectar dependencias manualmente
    public VacunaService(VacunaRepository vacunaRepository, AnimalService animalService,
                         ApplicationEventPublisher eventPublisher, NotificacionPushService notificacionPushService) {
        this.vacunaRepository = vacunaRepository;
        this.animalService = animalService;
        this.eventPublisher = eventPublisher;
        this.notificacionPushService = notificacionPushService;
    }

    // Obtener vacunas por animal
    public List<Vacuna> obtenerVacunasPorAnimal(Long animalId) {
        Animal animal = animalService.obtenerAnimalPorId(animalId);
        if (animal == null) {
            throw new NotFoundException("Animal no encontrado con ID: " + animalId);
        }
        return animal.getVacunas();
    }

    // Guardar una nueva vacuna
    public Vacuna guardarVacuna(VacunaRequestDto vacunaDto) {
        Animal animal = animalService.obtenerAnimalPorId(vacunaDto.getAnimalId());
        if (animal == null) {
            throw new NotFoundException("Animal no encontrado con ID: " + vacunaDto.getAnimalId());
        }

        if (vacunaDto.getNombre() == null || vacunaDto.getFechaAplicacion() == null) {
            throw new BadRequestException("Faltan datos obligatorios para registrar la vacuna.");
        }

        Vacuna vacuna = new Vacuna();
        vacuna.setNombre(vacunaDto.getNombre());
        vacuna.setFechaAplicacion(vacunaDto.getFechaAplicacion());
        vacuna.setAnimal(animal);

        // Guardar la vacuna en la base de datos
        Vacuna vacunaGuardada = vacunaRepository.save(vacuna);

        // Enviar correos y notificaciones
        enviarNotificacionesAsync(animal, vacuna, "Nueva vacuna registrada para " + animal.getNombre());

        return vacunaGuardada;
    }

    // Eliminar una vacuna
    public void eliminarVacuna(Long id) {
        Vacuna vacuna = vacunaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Vacuna no encontrada con ID: " + id));

        Animal animal = vacuna.getAnimal();

        vacunaRepository.deleteById(id);

        enviarNotificacionesAsync(animal, vacuna, "Vacuna eliminada para " + animal.getNombre());
    }

    // Método programado para enviar recordatorios de vacunas
    @Scheduled(cron = "0 0 8 * * ?") // Cada día a las 8 AM
    public void enviarRecordatoriosVacunas() {
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusDays(7);

        List<Vacuna> vacunasProximas = vacunaRepository.findVacunasProximas(today, nextWeek);

        if (vacunasProximas.isEmpty()) {
            throw new NotFoundException("No se encontraron vacunas próximas en la semana.");
        }

        for (Vacuna vacuna : vacunasProximas) {
            Animal animal = vacuna.getAnimal();
            enviarNotificacionesAsync(animal, vacuna, "Recordatorio: Vacuna próxima para " + animal.getNombre());
        }
    }

    // Método auxiliar asíncrono para enviar correos y notificaciones push
    @Async
    protected void enviarNotificacionesAsync(@NotNull Animal animal, Vacuna vacuna, String subject) {
        Usuario adoptante = animal.getAdoptante();
        if (adoptante == null) {
            throw new NotFoundException("Adoptante no encontrado para el animal: " + animal.getNombre());
        }

        String emailSubject = subject;
        String emailBody = "Estimado " + adoptante.getNombre() + ",\n\n" +
                "Se ha realizado la siguiente acción en la vacunación de su mascota " + animal.getNombre() + ":\n" +
                "Nombre de la vacuna: " + vacuna.getNombre() + "\n" +
                "Fecha de aplicación: " + vacuna.getFechaAplicacion() + "\n\n" +
                "Saludos,\n" +
                "Equipo de Adopción y Seguimiento de Animales";

        eventPublisher.publishEvent(new EmailEvent(adoptante.getEmail(), emailSubject, emailBody));

        if (adoptante.getToken() != null && !adoptante.getToken().isEmpty()) {
            String pushTitle = subject;
            String pushBody = "Se ha registrado una acción relacionada con la vacuna de tu mascota " + animal.getNombre() +
                    ". Vacuna: " + vacuna.getNombre() + ". Fecha de aplicación: " + vacuna.getFechaAplicacion();
            notificacionPushService.enviarNotificacion(adoptante.getToken(), pushTitle, pushBody);
        }
    }

    // Convertir vacuna a DTO de respuesta
    public VacunaResponseDto convertirVacunaAResponseDto(Vacuna vacuna) {
        if (vacuna == null) {
            throw new NotFoundException("Vacuna no encontrada");
        }

        VacunaResponseDto responseDto = new VacunaResponseDto();
        responseDto.setId(vacuna.getId());
        responseDto.setNombre(vacuna.getNombre());
        responseDto.setFechaAplicacion(vacuna.getFechaAplicacion());
        responseDto.setAnimalId(vacuna.getAnimal().getId());
        return responseDto;
    }
}
