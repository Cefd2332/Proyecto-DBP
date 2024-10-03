package org.e2e.e2e.RegistroSalud;

import lombok.RequiredArgsConstructor;
import org.e2e.e2e.Animal.Animal;
import org.e2e.e2e.Animal.AnimalService;
import org.e2e.e2e.Email.EmailEvent;
import org.e2e.e2e.Usuario.Usuario;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegistroSaludService {

    private final RegistroSaludRepository registroSaludRepository;
    private final AnimalService animalService;
    private final ApplicationEventPublisher eventPublisher;

    public List<RegistroSaludResponseDto> obtenerHistorialMedico(Long animalId) {
        Animal animal = animalService.obtenerAnimalPorId(animalId);
        return animal.getHistorialMedico().stream()
                .map(this::convertirRegistroSaludAResponseDto)
                .collect(Collectors.toList());
    }

    public RegistroSaludResponseDto guardarRegistroSalud(RegistroSaludRequestDto registroSaludDto) {
        Animal animal = animalService.obtenerAnimalPorId(registroSaludDto.getAnimalId());

        RegistroSalud registroSalud = new RegistroSalud();
        registroSalud.setDescripcion(registroSaludDto.getDescripcion());
        registroSalud.setFechaConsulta(registroSaludDto.getFechaConsulta());
        registroSalud.setVeterinario(registroSaludDto.getVeterinario());
        registroSalud.setAnimal(animal);

        // Guardar el registro de salud en la base de datos
        RegistroSalud registroGuardado = registroSaludRepository.save(registroSalud);

        // Enviar notificación por correo electrónico
        Usuario adoptante = animal.getAdoptante();
        String emailSubject = "Nuevo registro de salud para " + animal.getNombre();
        String emailBody = "Estimado " + adoptante.getNombre() + ",\n\n" +
                "Se ha registrado una nueva consulta de salud para su mascota " + animal.getNombre() + ".\n" +
                "Veterinario: " + registroSalud.getVeterinario() + "\n" +
                "Fecha de consulta: " + registroSalud.getFechaConsulta() + "\n" +
                "Descripción: " + registroSalud.getDescripcion() + "\n\n" +
                "Saludos,\n" +
                "Equipo de Adopción y Seguimiento de Animales";

        eventPublisher.publishEvent(new EmailEvent(adoptante.getEmail(), emailSubject, emailBody));

        return convertirRegistroSaludAResponseDto(registroGuardado);
    }

    public void eliminarRegistroSalud(Long id) {
        registroSaludRepository.deleteById(id);
    }

    public RegistroSaludResponseDto convertirRegistroSaludAResponseDto(RegistroSalud registroSalud) {
        RegistroSaludResponseDto responseDto = new RegistroSaludResponseDto();
        responseDto.setId(registroSalud.getId());
        responseDto.setDescripcion(registroSalud.getDescripcion());
        responseDto.setFechaConsulta(registroSalud.getFechaConsulta());
        responseDto.setVeterinario(registroSalud.getVeterinario());
        responseDto.setAnimalId(registroSalud.getAnimal().getId());
        return responseDto;
    }
}
