package org.e2e.e2e.Vacuna;

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
public class VacunaService {

    private final VacunaRepository vacunaRepository;
    private final AnimalService animalService;
    private final ApplicationEventPublisher eventPublisher;

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
}
