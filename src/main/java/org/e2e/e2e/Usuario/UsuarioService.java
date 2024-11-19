package org.e2e.e2e.Usuario;

import org.e2e.e2e.Adoptante.Adoptante;
import org.e2e.e2e.Adoptante.AdoptanteRepository;
import org.e2e.e2e.Adoptante.AdoptanteResponseDto;
import org.e2e.e2e.Animal.Animal;
import org.e2e.e2e.Animal.AnimalRepository;
import org.e2e.e2e.Animal.AnimalResponseDto;
import org.e2e.e2e.Email.EmailEvent;
import org.e2e.e2e.Notificacion.NotificacionPushService;
import org.e2e.e2e.exceptions.NotFoundException;
import org.e2e.e2e.exceptions.BadRequestException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final NotificacionPushService notificacionPushService;
    private final AdoptanteRepository adoptanteRepository;
    private final AnimalRepository animalRepository;

    // Constructor manual para inyectar dependencias
    public UsuarioService(UsuarioRepository usuarioRepository,
                          ApplicationEventPublisher eventPublisher,
                          NotificacionPushService notificacionPushService, AdoptanteRepository adoptanteRepository, AnimalRepository animalRepository) {
        this.usuarioRepository = usuarioRepository;
        this.eventPublisher = eventPublisher;
        this.notificacionPushService = notificacionPushService;
        this.adoptanteRepository = adoptanteRepository;
        this.animalRepository = animalRepository;
    }

    // Actualizar perfil del usuario
    public void actualizarPerfil(UsuarioRequestDto usuarioDto) {
        Usuario usuario = usuarioRepository.findByEmail(usuarioDto.getEmail())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado."));

        if (usuarioDto.getNombre() != null && !usuarioDto.getNombre().isEmpty()) {
            usuario.setNombre(usuarioDto.getNombre());
        }

        if (usuarioDto.getPassword() != null && !usuarioDto.getPassword().isEmpty()) {
            usuario.setPassword(usuarioDto.getPassword()); // Aquí aplica un encoder si tienes contraseñas cifradas
        }

        usuarioRepository.save(usuario);
    }

    // Verificar si la contraseña actual es correcta
    @Transactional
    public void cambiarContrasena(CambiarContrasenaRequest request) {
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + request.getUsuarioId()));

        // Verificar si la contraseña actual es correcta
        if (!usuario.getPassword().equals(request.getContrasenaActual())) {
            throw new BadRequestException("La contraseña actual es incorrecta.");
        }

        // Cambiar la contraseña
        usuario.setPassword(request.getNuevaContrasena()); // Asegúrate de encriptarla si estás usando un método de cifrado
        usuarioRepository.save(usuario);
    }

    // Obtener historial de adoptantes
    public List<AdoptanteResponseDto> obtenerHistorialAdoptantes() {
        List<Adoptante> adoptantes = adoptanteRepository.findAll();
        return adoptantes.stream()
                .map(adoptante -> new AdoptanteResponseDto(
                        adoptante.getId(),
                        adoptante.getNombre(),
                        adoptante.getEmail()
                ))
                .collect(Collectors.toList());
    }

    // Obtener historial de animales
    public List<AnimalResponseDto> obtenerHistorialAnimales() {
        List<Animal> animales = animalRepository.findAll();
        return animales.stream()
                .map(animal -> new AnimalResponseDto(
                        animal.getId(),
                        animal.getNombre(),
                        animal.getEspecie(),
                        animal.getEstadoSalud()
                ))
                .collect(Collectors.toList());
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


    }

    public Usuario obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException("Usuario no encontrado con email: " + email));
    }
}
