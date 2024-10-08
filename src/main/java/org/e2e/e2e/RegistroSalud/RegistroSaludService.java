package org.e2e.e2e.RegistroSalud;

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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegistroSaludService {

    private final RegistroSaludRepository registroSaludRepository;
    private final AnimalService animalService;
    private final ApplicationEventPublisher eventPublisher;
    private final NotificacionPushService notificacionPushService;

    // Obtener historial médico de un animal
    public List<RegistroSaludResponseDto> obtenerHistorialMedico(Long animalId) {
        Animal animal = animalService.obtenerAnimalPorId(animalId);
        if (animal == null) {
            throw new NotFoundException("Animal no encontrado con ID: " + animalId);
        }
        return animal.getHistorialMedico().stream()
                .map(this::convertirRegistroSaludAResponseDto)
                .collect(Collectors.toList());
    }

    // Guardar un nuevo registro de salud
    public RegistroSaludResponseDto guardarRegistroSalud(RegistroSaludRequestDto registroSaludDto) {
        Animal animal = animalService.obtenerAnimalPorId(registroSaludDto.getAnimalId());
        if (animal == null) {
            throw new NotFoundException("Animal no encontrado con ID: " + registroSaludDto.getAnimalId());
        }

        // Verificar si ya existe un registro en la misma fecha para evitar conflictos
        boolean existeRegistro = registroSaludRepository.existsByFechaConsultaAndAnimalId(
                registroSaludDto.getFechaConsulta(), animal.getId());

        if (existeRegistro) {
            throw new ConflictException("Ya existe un registro de salud para este animal en la fecha especificada.");
        }

        RegistroSalud registroSalud = new RegistroSalud();
        registroSalud.setDescripcion(registroSaludDto.getDescripcion());
        registroSalud.setFechaConsulta(registroSaludDto.getFechaConsulta());
        registroSalud.setVeterinario(registroSaludDto.getVeterinario());
        registroSalud.setAnimal(animal);

        // Guardar el registro de salud en la base de datos
        RegistroSalud registroGuardado = registroSaludRepository.save(registroSalud);

        // Enviar correos y notificaciones de forma asíncrona
        enviarNotificacionesAsync(animal, registroSalud, "Nuevo registro de salud para " + animal.getNombre());

        return convertirRegistroSaludAResponseDto(registroGuardado);
    }

    // Eliminar un registro de salud
    public void eliminarRegistroSalud(Long id) {
        RegistroSalud registroSalud = registroSaludRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Registro de salud no encontrado con ID: " + id));

        // Obtener el animal asociado al registro de salud
        Animal animal = registroSalud.getAnimal();

        // Eliminar el registro de salud
        registroSaludRepository.deleteById(id);

        // Enviar correo sobre la eliminación del registro de salud de forma asíncrona
        enviarNotificacionesAsync(animal, registroSalud, "Registro de salud eliminado para " + animal.getNombre());
    }

    // Método auxiliar para enviar notificaciones (correo y push) de manera asíncrona
    @Async
    public void enviarNotificacionesAsync(Animal animal, RegistroSalud registroSalud, String subject) {
        Usuario adoptante = animal.getAdoptante();

        // Enviar correo electrónico
        String emailSubject = subject;
        String emailBody = "Estimado " + adoptante.getNombre() + ",\n\n" +
                "Se ha realizado la siguiente acción en la salud de su mascota " + animal.getNombre() + ":\n" +
                "Veterinario: " + registroSalud.getVeterinario() + "\n" +
                "Fecha de consulta: " + registroSalud.getFechaConsulta() + "\n" +
                "Descripción: " + registroSalud.getDescripcion() + "\n\n" +
                "Saludos,\n" +
                "Equipo de Adopción y Seguimiento de Animales";

        eventPublisher.publishEvent(new EmailEvent(adoptante.getEmail(), emailSubject, emailBody));

        // Enviar notificación push al adoptante si tiene un token válido
        if (adoptante.getToken() != null && !adoptante.getToken().isEmpty()) {
            String pushTitle = subject;
            String pushBody = "Se ha realizado una acción en la salud de tu mascota " + animal.getNombre() +
                    ". Veterinario: " + registroSalud.getVeterinario() +
                    ". Fecha de consulta: " + registroSalud.getFechaConsulta();
            notificacionPushService.enviarNotificacion(adoptante.getToken(), pushTitle, pushBody);
        }
    }

    // Convertir un registro de salud a DTO de respuesta
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
