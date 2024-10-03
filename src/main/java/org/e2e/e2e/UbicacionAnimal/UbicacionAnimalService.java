package org.e2e.e2e.UbicacionAnimal;

import lombok.RequiredArgsConstructor;
import org.e2e.e2e.Animal.Animal;
import org.e2e.e2e.Animal.AnimalService;
import org.e2e.e2e.Email.EmailEvent;
import org.e2e.e2e.Usuario.Usuario;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UbicacionAnimalService {

    private final UbicacionAnimalRepository ubicacionAnimalRepository;
    private final AnimalService animalService;
    private final ApplicationEventPublisher eventPublisher;

    public List<UbicacionAnimal> obtenerUbicaciones(Long animalId) {
        Animal animal = animalService.obtenerAnimalPorId(animalId);
        return animal.getUbicaciones();
    }

    public UbicacionAnimal guardarUbicacion(UbicacionAnimalRequestDto ubicacionDto) {
        Animal animal = animalService.obtenerAnimalPorId(ubicacionDto.getAnimalId());

        UbicacionAnimal ubicacion = new UbicacionAnimal();
        ubicacion.setLatitud(ubicacionDto.getLatitud());
        ubicacion.setLongitud(ubicacionDto.getLongitud());
        ubicacion.setFechaHora(LocalDateTime.now());
        ubicacion.setAnimal(animal);

        // Guardar la ubicación en la base de datos
        UbicacionAnimal ubicacionGuardada = ubicacionAnimalRepository.save(ubicacion);

        // Obtener el adoptante (dueño) del animal
        Usuario adoptante = animal.getAdoptante();
        String emailSubject = "Nueva ubicación registrada para " + animal.getNombre();
        String emailBody = "Estimado " + adoptante.getNombre() + ",\n\n" +
                "Se ha registrado una nueva ubicación para su mascota " + animal.getNombre() + ".\n" +
                "Latitud: " + ubicacion.getLatitud() + "\n" +
                "Longitud: " + ubicacion.getLongitud() + "\n" +
                "Fecha y hora: " + ubicacion.getFechaHora() + "\n\n" +
                "Saludos,\n" +
                "Equipo de Adopción y Seguimiento de Animales";

        // Disparar el evento de correo electrónico
        eventPublisher.publishEvent(new EmailEvent(adoptante.getEmail(), emailSubject, emailBody));

        return ubicacionGuardada;
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
