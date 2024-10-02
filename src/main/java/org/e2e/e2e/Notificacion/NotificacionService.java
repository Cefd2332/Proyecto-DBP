package org.e2e.e2e.Notificacion;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final UsuarioService usuarioService;

    public List<Notificacion> obtenerNotificacionesPorUsuario(Long usuarioId) {
        return usuarioService.obtenerUsuarioPorId(usuarioId).getNotificaciones();
    }

    public Notificacion guardarNotificacion(Notificacion notificacion) {
        return notificacionRepository.save(notificacion);
    }
}
