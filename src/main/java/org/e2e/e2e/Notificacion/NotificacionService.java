package org.e2e.e2e.Notificacion;

import lombok.RequiredArgsConstructor;
import org.e2e.e2e.Usuario.Usuario;
import org.e2e.e2e.Usuario.UsuarioService;
import org.e2e.e2e.exceptions.NotFoundException;  // Importar la excepción de no encontrado
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final UsuarioService usuarioService;
    private final NotificacionPushService notificacionPushService;  // Inyectamos el servicio de notificaciones push

    // Obtener las notificaciones por usuario
    public List<Notificacion> obtenerNotificacionesPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(usuarioId);
        return usuario.getNotificaciones();  // Suponiendo que la entidad Usuario tiene una lista de notificaciones
    }

    // Enviar notificación y guardarla en la base de datos
    public Notificacion enviarNotificacion(NotificacionRequestDto notificacionDto) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(notificacionDto.getUsuarioId());

        Notificacion notificacion = new Notificacion();
        notificacion.setMensaje(notificacionDto.getMensaje());
        notificacion.setUsuario(usuario);
        notificacion.setFechaEnvio(LocalDateTime.now());
        notificacion.setEnviada(true);

        // Guardar la notificación en la base de datos
        Notificacion notificacionGuardada = notificacionRepository.save(notificacion);

        // Verificar si el usuario tiene un token FCM válido
        if (usuario.getToken() != null && !usuario.getToken().isEmpty()) {
            String pushTitle = "Nueva Notificación";
            String pushBody = notificacionDto.getMensaje();  // El mensaje de la notificación será el cuerpo del push
            notificacionPushService.enviarNotificacion(usuario.getToken(), pushTitle, pushBody);
        }

        return notificacionGuardada;
    }

    // Convertir una entidad Notificacion a DTO de respuesta
    public NotificacionResponseDto convertirNotificacionAResponseDto(Notificacion notificacion) {
        NotificacionResponseDto responseDto = new NotificacionResponseDto();
        responseDto.setId(notificacion.getId());
        responseDto.setMensaje(notificacion.getMensaje());
        responseDto.setFechaEnvio(notificacion.getFechaEnvio());
        responseDto.setEnviada(notificacion.isEnviada());
        responseDto.setUsuarioId(notificacion.getUsuario().getId());
        return responseDto;
    }
}
