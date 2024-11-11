package org.e2e.e2e.UbicacionAnimal;

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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UbicacionAnimalService {

    private final UbicacionAnimalRepository ubicacionAnimalRepository;
    private final AnimalService animalService;
    private final ApplicationEventPublisher eventPublisher;
    private final NotificacionPushService notificacionPushService;

    // Constructor manual para la inyección de dependencias
    public UbicacionAnimalService(UbicacionAnimalRepository ubicacionAnimalRepository,
                                  AnimalService animalService,
                                  ApplicationEventPublisher eventPublisher,
                                  NotificacionPushService notificacionPushService) {
        this.ubicacionAnimalRepository = ubicacionAnimalRepository;
        this.animalService = animalService;
        this.eventPublisher = eventPublisher;
        this.notificacionPushService = notificacionPushService;
    }

    // Obtener ubicaciones por animal
    public List<UbicacionAnimalResponseDto> obtenerUbicaciones(Long animalId) {
        Animal animal = animalService.obtenerAnimalPorId(animalId);
        if (animal == null) {
            throw new NotFoundException("Animal no encontrado con ID: " + animalId);
        }

        return animal.getUbicaciones().stream()
                .map(this::convertirUbicacionAResponseDto)
                .collect(Collectors.toList());
    }

    // Guardar una nueva ubicación
    public UbicacionAnimalResponseDto guardarUbicacion(UbicacionAnimalRequestDto ubicacionDto) {
        Animal animal = animalService.obtenerAnimalPorId(ubicacionDto.getAnimalId());
        if (animal == null) {
            throw new NotFoundException("Animal no encontrado con ID: " + ubicacionDto.getAnimalId());
        }

        boolean existeUbicacion = ubicacionAnimalRepository.existsByLatitudAndLongitudAndAnimalId(
                ubicacionDto.getLatitud(), ubicacionDto.getLongitud(), animal.getId());

        if (existeUbicacion) {
            throw new ConflictException("Ya existe una ubicación registrada con las mismas coordenadas para este animal.");
        }

        UbicacionAnimal ubicacion = new UbicacionAnimal();
        ubicacion.setLatitud(ubicacionDto.getLatitud());
        ubicacion.setLongitud(ubicacionDto.getLongitud());
        ubicacion.setFechaHora(LocalDateTime.now());
        ubicacion.setAnimal(animal);

        UbicacionAnimal ubicacionGuardada = ubicacionAnimalRepository.save(ubicacion);

        enviarNotificacionesAsync(animal, ubicacion, "Nueva ubicación registrada para " + animal.getNombre());

        return convertirUbicacionAResponseDto(ubicacionGuardada);
    }

    // Método asíncrono para enviar correos y notificaciones
    @Async
    public void enviarNotificacionesAsync(Animal animal, UbicacionAnimal ubicacion, String subject) {
        Usuario adoptante = animal.getAdoptante();

        String emailSubject = subject;
        String emailBody = "Estimado " + adoptante.getNombre() + ",\n\n" +
                "Se ha registrado una nueva ubicación para su mascota " + animal.getNombre() + ".\n" +
                "Latitud: " + ubicacion.getLatitud() + "\n" +
                "Longitud: " + ubicacion.getLongitud() + "\n" +
                "Fecha y hora: " + ubicacion.getFechaHora() + "\n\n" +
                "Saludos,\n" +
                "Equipo de Adopción y Seguimiento de Animales";

        eventPublisher.publishEvent(new EmailEvent(adoptante.getEmail(), emailSubject, emailBody));

        if (adoptante.getToken() != null && !adoptante.getToken().isEmpty()) {
            String pushTitle = subject;
            String pushBody = "Se ha registrado una nueva ubicación para tu mascota " + animal.getNombre() +
                    ". Latitud: " + ubicacion.getLatitud() + ", Longitud: " + ubicacion.getLongitud() +
                    ". Fecha: " + ubicacion.getFechaHora();
            notificacionPushService.enviarNotificacion(adoptante.getToken(), pushTitle, pushBody);
        }
    }

    // Convertir una entidad UbicacionAnimal a DTO de respuesta
    public UbicacionAnimalResponseDto convertirUbicacionAResponseDto(UbicacionAnimal ubicacion) {
        UbicacionAnimalResponseDto responseDto = new UbicacionAnimalResponseDto();
        responseDto.setId(ubicacion.getId());
        responseDto.setLatitud(ubicacion.getLatitud());
        responseDto.setLongitud(ubicacion.getLongitud());
        responseDto.setFechaHora(ubicacion.getFechaHora());
        responseDto.setAnimalId(ubicacion.getAnimal().getId());
        return responseDto;
    }

    // Eliminar una ubicación por ID
    public void eliminarUbicacion(Long id) {
        if (!ubicacionAnimalRepository.existsById(id)) {
            throw new NotFoundException("Ubicación no encontrada con ID: " + id);
        }
        ubicacionAnimalRepository.deleteById(id);
    }
}
