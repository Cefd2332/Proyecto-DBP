package org.e2e.e2e.Notificacion;

import org.e2e.e2e.Adoptante.Adoptante;
import org.e2e.e2e.Adoptante.AdoptanteService;
import org.e2e.e2e.exceptions.NotFoundException;  // Importar la excepción de no encontrado
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Servicio para manejar las notificaciones de los adoptantes.
 */
@Service
public class NotificacionService {

    private static final Logger logger = LoggerFactory.getLogger(NotificacionService.class);

    private final NotificacionRepository notificacionRepository;
    private final AdoptanteService adoptanteService;
    private final NotificacionPushService notificacionPushService;

    /**
     * Constructor que inyecta todas las dependencias necesarias.
     *
     * @param notificacionRepository    Repositorio para manejar notificaciones.
     * @param adoptanteService          Servicio para manejar operaciones relacionadas con adoptantes.
     * @param notificacionPushService   Servicio para enviar notificaciones push.
     */
    public NotificacionService(NotificacionRepository notificacionRepository,
                               AdoptanteService adoptanteService,
                               NotificacionPushService notificacionPushService) {
        this.notificacionRepository = notificacionRepository;
        this.adoptanteService = adoptanteService;
        this.notificacionPushService = notificacionPushService;
    }

    /**
     * Obtener las notificaciones por adoptante.
     *
     * @param adoptanteId ID del adoptante.
     * @return Lista de notificaciones del adoptante.
     * @throws NotFoundException Si el adoptante no se encuentra.
     */
    public List<Notificacion> obtenerNotificacionesPorAdoptante(Long adoptanteId) {
        Adoptante adoptante = adoptanteService.obtenerAdoptantePorId(adoptanteId);
        List<Notificacion> notificaciones = notificacionRepository.findByAdoptante(adoptante);
        logger.info("Obtenidas {} notificaciones para Adoptante ID: {}", notificaciones.size(), adoptanteId);
        return notificaciones;
    }

    /**
     * Enviar notificación y guardarla en la base de datos.
     *
     * @param notificacionDto DTO con los datos de la notificación.
     * @return Notificación guardada.
     * @throws NotFoundException Si el adoptante no se encuentra.
     */
    public Notificacion enviarNotificacion(NotificacionRequestDto notificacionDto) {
        Adoptante adoptante = adoptanteService.obtenerAdoptantePorId(notificacionDto.getAdoptanteId());

        Notificacion notificacion = new Notificacion();
        notificacion.setMensaje(notificacionDto.getMensaje());
        notificacion.setAdoptante(adoptante);
        notificacion.setFechaEnvio(LocalDateTime.now());
        notificacion.setEnviada(true);

        // Guardar la notificación en la base de datos
        Notificacion notificacionGuardada = notificacionRepository.save(notificacion);
        logger.info("Notificación guardada con ID: {} para Adoptante ID: {}", notificacionGuardada.getId(), adoptante.getId());

        // Verificar si el adoptante tiene un token FCM válido
        if (adoptante.getDeviceToken() != null && !adoptante.getDeviceToken().isEmpty()) {
            String pushTitle = "Nueva Notificación";
            String pushBody = notificacionDto.getMensaje();  // El mensaje de la notificación será el cuerpo del push
            notificacionPushService.enviarNotificacion(adoptante, pushTitle, pushBody);
            logger.info("Notificación push enviada a Adoptante ID: {}", adoptante.getId());
        } else {
            logger.warn("No se pudo enviar notificación push a Adoptante ID {}: No hay un token de dispositivo registrado.", adoptante.getId());
        }

        return notificacionGuardada;
    }

    /**
     * Convertir una entidad Notificacion a DTO de respuesta.
     *
     * @param notificacion Notificación a convertir.
     * @return DTO de respuesta.
     */
    public NotificacionResponseDto convertirNotificacionAResponseDto(Notificacion notificacion) {
        NotificacionResponseDto responseDto = new NotificacionResponseDto();
        responseDto.setId(notificacion.getId());
        responseDto.setMensaje(notificacion.getMensaje());
        responseDto.setFechaEnvio(notificacion.getFechaEnvio());
        responseDto.setEnviada(notificacion.isEnviada());
        responseDto.setAdoptanteId(notificacion.getAdoptante().getId());
        logger.info("Notificación convertida a DTO para Notificación ID: {}", notificacion.getId());
        return responseDto;
    }


}
