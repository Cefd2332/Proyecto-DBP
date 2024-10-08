package org.e2e.e2e.Adopcion;

import lombok.RequiredArgsConstructor;
import org.e2e.e2e.Animal.Animal;
import org.e2e.e2e.Animal.AnimalService;
import org.e2e.e2e.Email.EmailEvent;
import org.e2e.e2e.Notificacion.NotificacionPushService;
import org.e2e.e2e.Usuario.Usuario;
import org.e2e.e2e.Usuario.UsuarioService;
import org.e2e.e2e.exceptions.ConflictException;
import org.e2e.e2e.exceptions.NotFoundException;  // Excepción personalizada para recursos no encontrados
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
    private final NotificacionPushService notificacionPushService;  // Inyección del servicio de notificaciones push

    // Obtener todas las adopciones
    public List<Adopcion> obtenerTodasLasAdopciones() {
        return adopcionRepository.findAll();
    }

    // Registrar una adopción
    public Adopcion registrarAdopcion(AdopcionRequestDto adopcionDto) {
        Usuario adoptante = usuarioService.obtenerUsuarioPorId(adopcionDto.getAdoptanteId());
        Animal animal = animalService.obtenerAnimalPorId(adopcionDto.getAnimalId());

        // Verificar si el animal ya ha sido adoptado
        if (adopcionRepository.existsByAnimalId(animal.getId())) {
            throw new ConflictException("Este animal ya ha sido adoptado.");  // Excepción de conflicto
        }

        // Registrar la adopción
        Adopcion adopcion = new Adopcion();
        adopcion.setFechaAdopcion(adopcionDto.getFechaAdopcion());
        adopcion.setAdoptante(adoptante);
        adopcion.setAnimal(animal);

        Adopcion adopcionGuardada = adopcionRepository.save(adopcion);

        // Enviar correo de confirmación
        enviarNotificacionesAdopcion(adoptante, animal, adopcion);

        return adopcionGuardada;
    }

    // Obtener una adopción por ID
    public Adopcion obtenerAdopcionPorId(Long id) {
        return adopcionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Adopción no encontrada con ID: " + id));  // Excepción si no se encuentra la adopción
    }

    // Eliminar una adopción por ID
    public void eliminarAdopcion(Long id) {
        if (!adopcionRepository.existsById(id)) {
            throw new NotFoundException("Adopción no encontrada con ID: " + id);  // Excepción si no se encuentra la adopción
        }
        adopcionRepository.deleteById(id);
    }

    // Método para enviar notificaciones y correos electrónicos de adopción
    private void enviarNotificacionesAdopcion(Usuario adoptante, Animal animal, Adopcion adopcion) {
        String emailSubject = "Confirmación de adopción de " + animal.getNombre();
        String emailBody = "Estimado " + adoptante.getNombre() + ",\n\n" +
                "Felicitaciones, ha adoptado a " + animal.getNombre() + " con éxito.\n" +
                "Fecha de adopción: " + adopcion.getFechaAdopcion() + "\n\n" +
                "Saludos,\n" +
                "Equipo de Adopción y Seguimiento de Animales";

        // Disparar el evento de correo electrónico
        eventPublisher.publishEvent(new EmailEvent(adoptante.getEmail(), emailSubject, emailBody));

        // Enviar notificación push si el adoptante tiene un token válido
        if (adoptante.getToken() != null && !adoptante.getToken().isEmpty()) {
            String pushTitle = "Adopción exitosa de " + animal.getNombre();
            String pushBody = "Felicitaciones, ha adoptado a " + animal.getNombre() + " con éxito. ¡Gracias por brindar un hogar!";
            notificacionPushService.enviarNotificacion(adoptante.getToken(), pushTitle, pushBody);
        }
    }

    // Convertir una entidad Adopcion a DTO de respuesta
    public AdopcionResponseDto convertirAdopcionAResponseDto(Adopcion adopcion) {
        AdopcionResponseDto responseDto = new AdopcionResponseDto();
        responseDto.setId(adopcion.getId());
        responseDto.setFechaAdopcion(adopcion.getFechaAdopcion());
        responseDto.setAdoptanteId(adopcion.getAdoptante().getId());
        responseDto.setAnimalId(adopcion.getAnimal().getId());
        return responseDto;
    }
}
