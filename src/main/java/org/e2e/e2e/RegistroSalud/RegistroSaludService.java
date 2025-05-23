package org.e2e.e2e.RegistroSalud;

import org.e2e.e2e.Adoptante.Adoptante;
import org.e2e.e2e.Animal.Animal;
import org.e2e.e2e.Animal.AnimalService;
import org.e2e.e2e.Email.EmailEvent;
import org.e2e.e2e.Notificacion.NotificacionPushService;
import org.e2e.e2e.exceptions.NotFoundException;
import org.e2e.e2e.exceptions.ConflictException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegistroSaludService {

    private static final Logger logger = LoggerFactory.getLogger(RegistroSaludService.class);

    private final RegistroSaludRepository registroSaludRepository;
    private final AnimalService animalService;
    private final ApplicationEventPublisher eventPublisher;
    private final NotificacionPushService notificacionPushService;

    /**
     * Constructor que inyecta todas las dependencias necesarias.
     *
     * @param registroSaludRepository Repositorio para manejar registros de salud.
     * @param animalService           Servicio para manejar operaciones relacionadas con animales.
     * @param eventPublisher          Publicador de eventos para enviar correos electrónicos.
     * @param notificacionPushService Servicio para enviar notificaciones push.
     */
    public RegistroSaludService(RegistroSaludRepository registroSaludRepository,
                                AnimalService animalService,
                                ApplicationEventPublisher eventPublisher,
                                NotificacionPushService notificacionPushService) {
        this.registroSaludRepository = registroSaludRepository;
        this.animalService = animalService;
        this.eventPublisher = eventPublisher;
        this.notificacionPushService = notificacionPushService;
    }

    /**
     * Obtener el historial médico de un animal por su ID.
     *
     * @param animalId ID del animal.
     * @return Lista de registros de salud.
     */
    public List<RegistroSaludResponseDto> obtenerHistorialMedico(Long animalId) {
        Animal animal = animalService.obtenerAnimalPorId(animalId);
        // No es necesario verificar null, ya que obtenerAnimalPorId lanza NotFoundException si no encuentra
        return animal.getHistorialMedico().stream()
                .map(this::convertirRegistroSaludAResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Guardar un nuevo registro de salud.
     *
     * @param registroSaludDto DTO con los datos del registro de salud.
     * @return DTO de respuesta con los datos del registro guardado.
     */
    public RegistroSaludResponseDto guardarRegistroSalud(RegistroSaludRequestDto registroSaludDto) {
        Animal animal = animalService.obtenerAnimalPorId(registroSaludDto.getAnimalId());

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

    /**
     * Eliminar un registro de salud por su ID.
     *
     * @param id ID del registro de salud a eliminar.
     */
    public void eliminarRegistroSalud(Long id) {
        RegistroSalud registroSalud = registroSaludRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Registro de salud no encontrado con ID: " + id));

        // Obtener el animal asociado al registro de salud
        Animal animal = registroSalud.getAnimal();

        // Eliminar el registro de salud
        registroSaludRepository.deleteById(id);

        // Enviar notificación sobre la eliminación del registro de salud de forma asíncrona
        enviarNotificacionesAsync(animal, registroSalud, "Registro de salud eliminado para " + animal.getNombre());
    }

    /**
     * Método auxiliar para enviar notificaciones (correo y push) de manera asíncrona.
     *
     * @param animal        Animal asociado al registro de salud.
     * @param registroSalud  Registro de salud que se ha creado o eliminado.
     * @param subject       Asunto de la notificación.
     */
    @Async
    public void enviarNotificacionesAsync(Animal animal, RegistroSalud registroSalud, String subject) {
        Adoptante adoptante = animal.getAdoptante();

        // Enviar correo electrónico
        String emailSubject = subject;
        String emailBody = "Estimado " + adoptante.getNombre() + ",\n\n" +
                "Se ha realizado la siguiente acción en la salud de su mascota " + animal.getNombre() + ":\n" +
                "Veterinario: " + registroSalud.getVeterinario() + "\n" +
                "Fecha de consulta: " + registroSalud.getFechaConsulta() + "\n" +
                "Descripción: " + registroSalud.getDescripcion() + "\n\n" +
                "Saludos,\n" +
                "Equipo de Adopción y Seguimiento de Animales";

        // Publicar evento de envío de email
        eventPublisher.publishEvent(new EmailEvent(adoptante.getEmail(), emailSubject, emailBody));

        // Enviar notificación push al adoptante si tiene un token válido
        if (adoptante.getDeviceToken() != null && !adoptante.getDeviceToken().isEmpty()) {
            String pushTitle = subject;
            String pushBody = "Se ha realizado una acción en la salud de tu mascota " + animal.getNombre() +
                    ". Veterinario: " + registroSalud.getVeterinario() +
                    ". Fecha de consulta: " + registroSalud.getFechaConsulta();
            notificacionPushService.enviarNotificacion(adoptante, pushTitle, pushBody);
        } else {
            logger.warn("No se pudo enviar notificación push a Adoptante ID {}: No hay un token de dispositivo registrado.", adoptante.getId());
        }
    }

    /**
     * Convertir un registro de salud a DTO de respuesta.
     *
     * @param registroSalud Registro de salud a convertir.
     * @return DTO de respuesta.
     */
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
