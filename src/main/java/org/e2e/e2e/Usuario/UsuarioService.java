package org.e2e.e2e.Usuario;

import org.e2e.e2e.Email.EmailEvent;
import org.e2e.e2e.Notificacion.NotificacionPushService;
import org.e2e.e2e.exceptions.NotFoundException;
import org.e2e.e2e.exceptions.BadRequestException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final NotificacionPushService notificacionPushService;

    // Constructor manual para inyectar dependencias
    public UsuarioService(UsuarioRepository usuarioRepository,
                          ApplicationEventPublisher eventPublisher,
                          NotificacionPushService notificacionPushService) {
        this.usuarioRepository = usuarioRepository;
        this.eventPublisher = eventPublisher;
        this.notificacionPushService = notificacionPushService;
    }

    // Obtener todos los usuarios
    public List<Usuario> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        if (usuarios.isEmpty()) {
            throw new NotFoundException("No se encontraron usuarios registrados.");
        }
        return usuarios;
    }

    // Obtener un usuario por ID
    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + id));
    }

    // Guardar un nuevo usuario
    @Transactional
    public Usuario guardarUsuario(UsuarioRequestDto usuarioDto) {
        if (usuarioDto.getNombre() == null || usuarioDto.getEmail() == null || usuarioDto.getDireccion() == null) {
            throw new BadRequestException("Faltan datos obligatorios para crear el usuario.");
        }

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
    @Transactional
    public Usuario actualizarUsuario(Long id, UsuarioRequestDto usuarioDto) {
        Usuario usuario = obtenerUsuarioPorId(id);

        if (usuarioDto.getNombre() == null || usuarioDto.getEmail() == null || usuarioDto.getDireccion() == null) {
            throw new BadRequestException("Faltan datos obligatorios para actualizar el usuario.");
        }

        usuario.setNombre(usuarioDto.getNombre());
        usuario.setEmail(usuarioDto.getEmail());
        usuario.setDireccion(usuarioDto.getDireccion());

        Usuario usuarioActualizado = usuarioRepository.save(usuario);

        // Enviar notificaciones de actualización
        enviarNotificacionesAsync(usuarioActualizado, "Usuario actualizado: " + usuarioActualizado.getNombre());

        return usuarioActualizado;
    }

    // Eliminar un usuario por ID
    @Transactional
    public void eliminarUsuario(Long id) {
        Usuario usuario = obtenerUsuarioPorId(id);

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
