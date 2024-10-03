package org.e2e.e2e.Adopcion;

import lombok.RequiredArgsConstructor;
import org.e2e.e2e.Animal.Animal;
import org.e2e.e2e.Animal.AnimalService;
import org.e2e.e2e.Email.EmailEvent;
import org.e2e.e2e.Usuario.Usuario;
import org.e2e.e2e.Usuario.UsuarioService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdopcionService {

    private final AdopcionRepository adopcionRepository;
    private final UsuarioService usuarioService;
    private final AnimalService animalService;
    private final ApplicationEventPublisher eventPublisher;

    public List<Adopcion> obtenerTodasLasAdopciones() {
        return adopcionRepository.findAll();
    }

    public Adopcion registrarAdopcion(AdopcionRequestDto adopcionDto) {
        Usuario adoptante = usuarioService.obtenerUsuarioPorId(adopcionDto.getAdoptanteId());
        Animal animal = animalService.obtenerAnimalPorId(adopcionDto.getAnimalId());

        Adopcion adopcion = new Adopcion();
        adopcion.setFechaAdopcion(adopcionDto.getFechaAdopcion());
        adopcion.setAdoptante(adoptante);
        adopcion.setAnimal(animal);

        // Guardar la adopción en la base de datos
        Adopcion adopcionGuardada = adopcionRepository.save(adopcion);

        // Enviar correo de confirmación de adopción
        String emailSubject = "Confirmación de adopción de " + animal.getNombre();
        String emailBody = "Estimado " + adoptante.getNombre() + ",\n\n" +
                "Felicitaciones, ha adoptado a " + animal.getNombre() + " con éxito.\n" +
                "Fecha de adopción: " + adopcion.getFechaAdopcion() + "\n\n" +
                "Saludos,\n" +
                "Equipo de Adopción y Seguimiento de Animales";

        // Disparar el evento de correo electrónico
        eventPublisher.publishEvent(new EmailEvent(adoptante.getEmail(), emailSubject, emailBody));

        return adopcionGuardada;
    }

    public Adopcion obtenerAdopcionPorId(Long id) {
        return adopcionRepository.findById(id).orElseThrow(() -> new RuntimeException("Adopción no encontrada"));
    }

    public void eliminarAdopcion(Long id) {
        adopcionRepository.deleteById(id);
    }

    public AdopcionResponseDto convertirAdopcionAResponseDto(Adopcion adopcion) {
        AdopcionResponseDto responseDto = new AdopcionResponseDto();
        responseDto.setId(adopcion.getId());
        responseDto.setFechaAdopcion(adopcion.getFechaAdopcion());
        responseDto.setAdoptanteId(adopcion.getAdoptante().getId());
        responseDto.setAnimalId(adopcion.getAnimal().getId());
        return responseDto;
    }
}
