package org.e2e.e2e.UbicacionAnimal;

import lombok.RequiredArgsConstructor;
import org.e2e.e2e.Animal.Animal;
import org.e2e.e2e.Animal.AnimalService;
import org.e2e.e2e.Email.EmailEvent;
import org.e2e.e2e.Notificacion.NotificacionPushService;
import org.e2e.e2e.Usuario.Usuario;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UbicacionAnimalService {

    private final UbicacionAnimalRepository ubicacionAnimalRepository;
    private final AnimalService animalService;
    private final ApplicationEventPublisher eventPublisher;
    private final NotificacionPushService notificacionPushService;  // Inyectamos el servicio de notificaciones push

    public List<UbicacionAnimalResponseDto> obtenerUbicaciones(Long animalId) {
        Animal animal = animalService.obtenerAnimalPorId(animalId);
        return animal.getUbicaciones().stream()
                .map(this::convertirUbicacionAResponseDto)
                .collect(Collectors.toList());
    }

    public UbicacionAnimalResponseDto guardarUbicacion(UbicacionAnimalRequestDto ubicacionDto) {
        Animal animal = animalService.obtenerAnimalPorId(ubicacionDto.getAnimalId());

        UbicacionAnimal ubicacion = new UbicacionAnimal();
        ubicacion.setLatitud(ubicacionDto.getLatitud());
        ubicacion.setLongitud(ubicacionDto.getLongitud());
        ubicacion.setFechaHora(LocalDateTime.now());
        ubicacion.setAnimal(animal);

        // Guardar la ubicación en la base de datos
        UbicacionAnimal ubicacionGuardada = ubicacionAnimalRepository.save(ubicacion);

        // Enviar correo electrónico al adoptante
        Usuario adoptante = animal.getAdoptante();
        String emailSubject = "Nueva ubicación registrada para " + animal.getNombre();
        String emailBody = "Estimado " + adoptante.getNombre() + ",\n\n" +
                "Se ha registrado una nueva ubicación para su mascota " + animal.getNombre() + ".\n" +
                "Latitud: " + ubicacion.getLatitud() + "\n" +
                "Longitud: " + ubicacion.getLongitud() + "\n" +
                "Fecha y hora: " + ubicacion.getFechaHora() + "\n\n" +
                "Saludos,\n" +
                "Equipo de Adopción y Seguimiento de Animales";

        eventPublisher.publishEvent(new EmailEvent(adoptante.getEmail(), emailSubject, emailBody));

        // Enviar notificación push al adoptante si tiene un token FCM válido
        if (adoptante.getToken() != null && !adoptante.getToken().isEmpty()) {
            String pushTitle = "Nueva ubicación para " + animal.getNombre();
            String pushBody = "Se ha registrado una nueva ubicación para tu mascota " + animal.getNombre() +
                    ". Latitud: " + ubicacion.getLatitud() + ", Longitud: " + ubicacion.getLongitud() +
                    ". Fecha: " + ubicacion.getFechaHora();
            notificacionPushService.enviarNotificacion(adoptante.getToken(), pushTitle, pushBody);
        }

        return convertirUbicacionAResponseDto(ubicacionGuardada);
    }

    public UbicacionAnimalResponseDto convertirUbicacionAResponseDto(UbicacionAnimal ubicacion) {
        UbicacionAnimalResponseDto responseDto = new UbicacionAnimalResponseDto();
        responseDto.setId(ubicacion.getId());
        responseDto.setLatitud(ubicacion.getLatitud());
        responseDto.setLongitud(ubicacion.getLongitud());
        responseDto.setFechaHora(ubicacion.getFechaHora());
        responseDto.setAnimalId(ubicacion.getAnimal().getId());
        return responseDto;
    }
}
