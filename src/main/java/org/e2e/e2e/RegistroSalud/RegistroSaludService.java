package org.e2e.e2e.RegistroSalud;

import lombok.RequiredArgsConstructor;
import org.e2e.e2e.Animal.Animal;
import org.e2e.e2e.Animal.AnimalService;
import org.e2e.e2e.Email.EmailEvent;
import org.e2e.e2e.Usuario.Usuario;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistroSaludService {

    private final RegistroSaludRepository registroSaludRepository;
    private final AnimalService animalService;
    private final ApplicationEventPublisher eventPublisher;

    public List<RegistroSalud> obtenerHistorialMedico(Long animalId) {
        Animal animal = animalService.obtenerAnimalPorId(animalId);
        return animal.getHistorialMedico();
    }

    public RegistroSalud guardarRegistroSalud(RegistroSaludRequestDto registroSaludDto) {
        Animal animal = animalService.obtenerAnimalPorId(registroSaludDto.getAnimalId());

        RegistroSalud registroSalud = new RegistroSalud();
        registroSalud.setDescripcion(registroSaludDto.getDescripcion());
        registroSalud.setFechaConsulta(registroSaludDto.getFechaConsulta());
        registroSalud.setVeterinario(registroSaludDto.getVeterinario());
        registroSalud.setAnimal(animal);

        // Guardar el registro de salud en la base de datos
        RegistroSalud registroGuardado = registroSaludRepository.save(registroSalud);

        // Obtener el adoptante (dueño) del animal
        Usuario adoptante = animal.getAdoptante();
        String emailSubject = "Nuevo registro de salud para " + animal.getNombre();
        String emailBody = "Estimado " + adoptante.getNombre() + ",\n\n" +
                "Se ha registrado una nueva consulta de salud para su mascota " + animal.getNombre() + ".\n" +
                "Veterinario: " + registroSalud.getVeterinario() + "\n" +
                "Fecha de consulta: " + registroSalud.getFechaConsulta() + "\n" +
                "Descripción: " + registroSalud.getDescripcion() + "\n\n" +
                "Saludos,\n" +
                "Equipo de Adopción y Seguimiento de Animales";

        // Disparar el evento de correo electrónico
        eventPublisher.publishEvent(new EmailEvent(adoptante.getEmail(), emailSubject, emailBody));

        return registroGuardado;
    }

    public void eliminarRegistroSalud(Long id) {
        registroSaludRepository.deleteById(id);
    }
}
