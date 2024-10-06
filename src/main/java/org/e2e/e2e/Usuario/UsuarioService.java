package org.e2e.e2e.Usuario;

import lombok.RequiredArgsConstructor;
import org.e2e.e2e.Email.EmailEvent;
import org.e2e.e2e.Notificacion.NotificacionPushService;
import org.e2e.e2e.exceptions.NotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final NotificacionPushService notificacionPushService;

    // Obtener todos los usuarios
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    // Obtener un usuario por ID
    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + id));
    }

    // Guardar un nuevo usuario
    public Usuario guardarUsuario(UsuarioRequestDto usuarioDto) {
        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioDto.getNombre());
        usuario.setEmail(usuarioDto.getEmail());
        usuario.setDireccion(usuarioDto.getDireccion());

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        // Enviar notificaciones de creación
        enviarNotificacionesAsync(usuarioGuardado, "Nuevo usuario registrado: " + usuarioGuardado.getNombre());

        return usuarioGuardado;
    }

    // Actualizar un usuario existente
    public Usuario actualizarUsuario(Long id, UsuarioRequestDto usuarioDto) {
        Usuario usuario = obtenerUsuarioPorId(id);  // Lanza excepción si no se encuentra
        usuario.setNombre(usuarioDto.getNombre());
        usuario.setEmail(usuarioDto.getEmail());
        usuario.setDireccion(usuarioDto.getDireccion());

        Usuario usuarioActualizado = usuarioRepository.save(usuario);

        // Enviar notificaciones de actualización
        enviarNotificacionesAsync(usuarioActualizado, "Usuario actualizado: " + usuarioActualizado.getNombre());

        return usuarioActualizado;
    }

    // Eliminar un usuario por ID
    public void eliminarUsuario(Long id) {
        Usuario usuario = obtenerUsuarioPorId(id);  // Lanza excepción si no se encuentra

        usuarioRepository.deleteById(id);

        // Enviar notificaciones de eliminación
        enviarNotificacionesAsync(usuario, "Usuario eliminado: " + usuario.getNombre());
    }

    // Método asíncrono para enviar correos y notificaciones
    @Async
    public void enviarNotificacionesAsync(Usuario usuario, String subject) {
        // Enviar correo electrónico
        String emailSubject = subject;
        String emailBody = "Estimado " + usuario.getNombre() + ",\n\n" +
                "Se ha realizado la siguiente acción en su cuenta:\n" +
                "Nombre: " + usuario.getNombre() + "\n" +
                "Email: " + usuario.getEmail() + "\n" +
                "Dirección: " + usuario.getDireccion() + "\n\n" +
                "Saludos,\n" +
                "Equipo de Gestión de Usuarios";

        eventPublisher.publishEvent(new EmailEvent(usuario.getEmail(), emailSubject, emailBody));

        // Enviar notificación push si el usuario tiene un token disponible
        if (usuario.getToken() != null && !usuario.getToken().isEmpty()) {
            String pushTitle = subject;
            String pushBody = "Se ha realizado una acción en tu cuenta:\n" +
                    "Nombre: " + usuario.getNombre() + "\n" +
                    "Email: " + usuario.getEmail();
            notificacionPushService.enviarNotificacion(usuario.getToken(), pushTitle, pushBody);
        }
    }
}
