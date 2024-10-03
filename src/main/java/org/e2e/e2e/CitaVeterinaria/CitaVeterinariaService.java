package org.e2e.e2e.CitaVeterinaria;

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
public class CitaVeterinariaService {

    private final CitaVeterinariaRepository citaVeterinariaRepository;
    private final AnimalService animalService;
    private final ApplicationEventPublisher eventPublisher;

    public List<CitaVeterinaria> obtenerCitasPorAnimal(Long animalId) {
        Animal animal = animalService.obtenerAnimalPorId(animalId);
        return animal.getCitasVeterinarias();
    }

    public CitaVeterinaria guardarCita(CitaVeterinariaRequestDto citaDto) {
        Animal animal = animalService.obtenerAnimalPorId(citaDto.getAnimalId());

        CitaVeterinaria cita = new CitaVeterinaria();
        cita.setFechaCita(citaDto.getFechaCita());
        cita.setVeterinario(citaDto.getVeterinario());
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

        return citaGuardada;
    }

    public void eliminarCita(Long id) {
        citaVeterinariaRepository.deleteById(id);
    }

    public CitaVeterinariaResponseDto convertirCitaAResponseDto(CitaVeterinaria cita) {
        CitaVeterinariaResponseDto responseDto = new CitaVeterinariaResponseDto();
        responseDto.setId(cita.getId());
        responseDto.setFechaCita(cita.getFechaCita());
        responseDto.setVeterinario(cita.getVeterinario());
        responseDto.setAnimalId(cita.getAnimal().getId());
        return responseDto;
    }
}
