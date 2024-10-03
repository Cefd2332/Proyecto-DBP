package org.e2e.e2e.Notificacion;

import lombok.RequiredArgsConstructor;
import org.e2e.e2e.Usuario.Usuario;
import org.e2e.e2e.Usuario.UsuarioService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final UsuarioService usuarioService;

    public List<Notificacion> obtenerNotificacionesPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(usuarioId);
        return usuario.getNotificaciones();  // Suponiendo que la entidad Usuario tiene una lista de notificaciones
    }

    public Notificacion enviarNotificacion(NotificacionRequestDto notificacionDto) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(notificacionDto.getUsuarioId());

        Notificacion notificacion = new Notificacion();
        notificacion.setMensaje(notificacionDto.getMensaje());
        notificacion.setUsuario(usuario);
        notificacion.setFechaEnvio(LocalDateTime.now());
        notificacion.setEnviada(true);

        return notificacionRepository.save(notificacion);
    }

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
