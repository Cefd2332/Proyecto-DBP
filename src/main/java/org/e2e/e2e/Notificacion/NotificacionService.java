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
        return usuarioService.obtenerUsuarioPorId(usuarioId).getNotificaciones();
    }

    public Notificacion guardarNotificacion(NotificacionRequestDto notificacionDto) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(notificacionDto.getUsuarioId());

        Notificacion notificacion = new Notificacion();
        notificacion.setMensaje(notificacionDto.getMensaje());
        notificacion.setUsuario(usuario);
        notificacion.setFechaEnvio(LocalDateTime.now());
        notificacion.setEnviada(true);  // Asumimos que la notificación se envía inmediatamente

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
