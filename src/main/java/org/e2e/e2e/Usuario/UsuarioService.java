// src/main/java/org/e2e/e2e/Usuario/UsuarioService.java

package org.e2e.e2e.Usuario;

import io.jsonwebtoken.io.IOException;
import jakarta.validation.Valid;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.web.multipart.MultipartFile;

import java.security.Key;
import java.util.Base64;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final NotificacionPushService notificacionPushService;
    private final AdoptanteRepository adoptanteRepository;
    private final AnimalRepository animalRepository;
    private final PasswordEncoder passwordEncoder;
    private final Key jwtSecretKey;
    private final AlmacenamientoService almacenamientoService; // Servicio para manejar archivos


    // Constructor manual para inyectar dependencias
    public UsuarioService(UsuarioRepository usuarioRepository,
                          ApplicationEventPublisher eventPublisher,
                          NotificacionPushService notificacionPushService,
                          AdoptanteRepository adoptanteRepository,
                          AnimalRepository animalRepository,
                          PasswordEncoder passwordEncoder,
                          AlmacenamientoService almacenamientoService) {
        this.usuarioRepository = usuarioRepository;
        this.eventPublisher = eventPublisher;
        this.notificacionPushService = notificacionPushService;
        this.adoptanteRepository = adoptanteRepository;
        this.animalRepository = animalRepository;
        this.passwordEncoder = passwordEncoder;
        this.almacenamientoService = almacenamientoService;
        // Inicializar la clave secreta para JWT (debe ser segura y almacenada de manera segura)
        this.jwtSecretKey = Keys.hmacShaKeyFor("TuClaveSecretaMuySeguraParaJWT1234567890".getBytes(StandardCharsets.UTF_8));
    }

    // Guardar un nuevo usuario
    @Transactional
    public Usuario guardarUsuario(UsuarioRequestDto usuarioDto) {
        // Validaciones básicas
        if (usuarioDto.getNombre() == null || usuarioDto.getNombre().isEmpty() ||
                usuarioDto.getEmail() == null || usuarioDto.getEmail().isEmpty() ||
                usuarioDto.getDireccion() == null || usuarioDto.getDireccion().isEmpty() ||
                usuarioDto.getPassword() == null || usuarioDto.getPassword().isEmpty()) {
            throw new BadRequestException("Faltan datos obligatorios para crear el usuario.");
        }

        // Verificar si el email ya está en uso
        if (usuarioRepository.findByEmail(usuarioDto.getEmail()).isPresent()) {
            throw new BadRequestException("El correo electrónico ya está en uso.");
        }

        // Crear y guardar el usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioDto.getNombre());
        usuario.setEmail(usuarioDto.getEmail());
        usuario.setDireccion(usuarioDto.getDireccion());
        usuario.setPassword(passwordEncoder.encode(usuarioDto.getPassword())); // Encriptar contraseña

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        // Enviar notificaciones de creación
        enviarNotificacionesAsync(usuarioGuardado, "Nuevo usuario registrado: " + usuarioGuardado.getNombre());

        return usuarioGuardado;
    }

    // Método para autenticar usuario y obtener ID encapsulado en AutenticacionResponseDto
    @Transactional(readOnly = true)
    public AutenticacionResponseDto autenticarYObtenerId(AutenticacionRequestDto autenticacionRequest) {
        Usuario usuario = usuarioRepository.findByEmail(autenticacionRequest.getEmail())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con email: " + autenticacionRequest.getEmail()));

        // Verificar que el nombre coincida
        if (!usuario.getNombre().equalsIgnoreCase(autenticacionRequest.getNombre())) {
            throw new BadRequestException("El nombre proporcionado no coincide.");
        }

        // Verificar que la contraseña sea correcta
        if (!passwordEncoder.matches(autenticacionRequest.getContrasena(), usuario.getPassword())) {
            throw new BadRequestException("La contraseña es incorrecta.");
        }

        // Autenticación exitosa, retornar el userId
        return new AutenticacionResponseDto(usuario.getId());
    }

    /**
     * Actualizar perfil del usuario sin necesidad de token JWT
     */
    @Transactional
    public void actualizarPerfil(Long userId, String nombre, String email, String direccion, MultipartFile fotoPerfil) {
        Usuario usuario = obtenerUsuarioPorId(userId);

        // Actualizar nombre
        if (nombre != null && !nombre.isEmpty()) {
            usuario.setNombre(nombre);
        }

        // Actualizar correo electrónico
        if (email != null && !email.isEmpty() && !usuario.getEmail().equalsIgnoreCase(email)) {
            // Verificar si el nuevo email ya está en uso
            if (usuarioRepository.findByEmail(email).isPresent()) {
                throw new BadRequestException("El correo electrónico ya está en uso.");
            }
            usuario.setEmail(email);
        }

        // Actualizar dirección
        if (direccion != null && !direccion.isEmpty()) {
            usuario.setDireccion(direccion);
        }

        // Manejar la imagen de perfil
        if (fotoPerfil != null && !fotoPerfil.isEmpty()) {
            try {
                usuario.setFotoPerfil(fotoPerfil.getBytes());
            } catch (IOException | java.io.IOException e) {
                throw new RuntimeException("Error al leer el archivo de imagen", e);
            }
        }

        usuarioRepository.save(usuario);
    }

    // Cambiar contraseña
    @Transactional
    public void cambiarContrasena(CambiarContrasenaRequest request) {
        // Buscar al usuario por ID
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + request.getUsuarioId()));

        // Verificar si la contraseña actual es correcta
        if (!passwordEncoder.matches(request.getContrasenaActual(), usuario.getPassword())) {
            throw new BadRequestException("La contraseña actual es incorrecta.");
        }

        // Cambiar y encriptar la nueva contraseña
        usuario.setPassword(passwordEncoder.encode(request.getNuevaContrasena()));
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

    // Eliminar un usuario por ID
    @Transactional
    public void eliminarUsuario(Long id) {
        Usuario usuario = obtenerUsuarioPorId(id);
        usuarioRepository.deleteById(id);
        // Enviar notificaciones de eliminación
        enviarNotificacionesAsync(usuario, "Usuario eliminado: " + usuario.getNombre());
    }

    // Método para extraer el email desde el token JWT
    public String obtenerEmailDesdeToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("email", String.class);
        } catch (Exception e) {
            throw new BadRequestException("Token JWT inválido.");
        }
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

    // Obtener un usuario por email
    public Usuario obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con email: " + email));
    }

    // Actualizar un usuario existente
    @Transactional
    public Usuario actualizarUsuario(Long id, @Valid UsuarioRequestDto usuarioDto) {
        Usuario usuario = obtenerUsuarioPorId(id);

        // Actualizar campos si no son nulos o vacíos
        if (usuarioDto.getNombre() != null && !usuarioDto.getNombre().isEmpty()) {
            usuario.setNombre(usuarioDto.getNombre());
        }

        if (usuarioDto.getEmail() != null && !usuarioDto.getEmail().isEmpty()) {
            // Verificar si el nuevo email ya está en uso por otro usuario
            if (!usuario.getEmail().equalsIgnoreCase(usuarioDto.getEmail()) &&
                    usuarioRepository.findByEmail(usuarioDto.getEmail()).isPresent()) {
                throw new BadRequestException("El correo electrónico ya está en uso.");
            }
            usuario.setEmail(usuarioDto.getEmail());
        }

        if (usuarioDto.getDireccion() != null && !usuarioDto.getDireccion().isEmpty()) {
            usuario.setDireccion(usuarioDto.getDireccion());
        }

        // Si se proporciona una nueva contraseña, actualizarla
        if (usuarioDto.getPassword() != null && !usuarioDto.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuarioDto.getPassword()));
        }

        // Guardar los cambios
        Usuario usuarioActualizado = usuarioRepository.save(usuario);

        // Enviar notificaciones de actualización
        enviarNotificacionesAsync(usuarioActualizado, "Usuario actualizado: " + usuarioActualizado.getNombre());

        return usuarioActualizado;
    }
}
