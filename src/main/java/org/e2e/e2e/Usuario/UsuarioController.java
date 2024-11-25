package org.e2e.e2e.Usuario;
// src/main/java/org/e2e/e2e/Usuario/UsuarioController.java

import jakarta.validation.Valid;
import org.e2e.e2e.Adoptante.AdoptanteResponseDto;
import org.e2e.e2e.Animal.AnimalResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    // Constructor manual sin Lombok
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> obtenerUsuarios() {
        List<UsuarioResponseDto> usuariosResponse = usuarioService.obtenerTodosLosUsuarios().stream()
                .map(usuario -> {
                    String fotoPerfilUrl = null;
                    if (usuario.getFotoPerfil() != null && usuario.getFotoPerfil().length > 0) {
                        String base64Image = Base64.getEncoder().encodeToString(usuario.getFotoPerfil());
                        fotoPerfilUrl = "data:image/jpeg;base64," + base64Image;
                    }
                    return new UsuarioResponseDto(
                            usuario.getId(),
                            usuario.getNombre(),
                            usuario.getEmail(),
                            usuario.getDireccion(),
                            usuario.getRoles().stream().collect(Collectors.toList()),
                            fotoPerfilUrl, fotoPerfilUrl
                    );
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuariosResponse);
    }

    // Obtener perfil del usuario actual
    @GetMapping("/perfil")
    public ResponseEntity<UsuarioResponseDto> obtenerPerfilUsuario(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        String emailUsuarioActual = usuarioService.obtenerEmailDesdeToken(token);

        Usuario usuario = usuarioService.obtenerUsuarioPorEmail(emailUsuarioActual);

        String fotoPerfilUrl = null;
        if (usuario.getFotoPerfil() != null && usuario.getFotoPerfil().length > 0) {
            String base64Image = Base64.getEncoder().encodeToString(usuario.getFotoPerfil());
            fotoPerfilUrl = "data:image/jpeg;base64," + base64Image;
        }

        UsuarioResponseDto usuarioResponse = new UsuarioResponseDto(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getDireccion(),
                new ArrayList<>(usuario.getRoles()),
                fotoPerfilUrl, fotoPerfilUrl
        );

        return ResponseEntity.ok(usuarioResponse);
    }

    // Obtener un usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> obtenerUsuarioPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);

        String fotoPerfilUrl = null;
        if (usuario.getFotoPerfil() != null && usuario.getFotoPerfil().length > 0) {
            String base64Image = Base64.getEncoder().encodeToString(usuario.getFotoPerfil());
            fotoPerfilUrl = "data:image/jpeg;base64," + base64Image;
        }

        UsuarioResponseDto usuarioResponse = new UsuarioResponseDto(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getDireccion(),
                new ArrayList<>(usuario.getRoles()),
                fotoPerfilUrl, fotoPerfilUrl
        );

        return ResponseEntity.ok(usuarioResponse);
    }

    // Actualizar perfil del usuario sin necesidad de token JWT
    @PutMapping(value = "/perfil", consumes = {"multipart/form-data"})
    public ResponseEntity<String> actualizarPerfil(
            @RequestParam("userId") Long userId,
            @RequestParam("nombre") String nombre,
            @RequestParam("email") String email,
            @RequestParam("direccion") String direccion,
            @RequestParam(value = "fotoPerfil", required = false) MultipartFile fotoPerfil) {

        System.out.println("Nombre recibido: " + nombre);
        System.out.println("Email recibido: " + email);
        System.out.println("Direccion recibida: " + direccion);
        System.out.println("FotoPerfil recibida: " + (fotoPerfil != null ? fotoPerfil.getOriginalFilename() : "Sin foto"));

        usuarioService.actualizarPerfil(userId, nombre, email, direccion, fotoPerfil);
        return ResponseEntity.ok("Perfil actualizado correctamente.");
    }

    // Cambiar contraseña
    @PutMapping("/perfil/cambiar-contrasena")
    public ResponseEntity<String> cambiarContrasena(@RequestBody CambiarContrasenaRequest cambiarContrasenaRequest, @RequestHeader("Authorization") String authorizationHeader) {
        // Extraer el token JWT del encabezado Authorization
        String token = authorizationHeader.replace("Bearer ", "");

        // Obtener el email del usuario actual desde el token JWT
        String emailUsuarioActual = usuarioService.obtenerEmailDesdeToken(token);

        // Obtener el usuario por email
        Usuario usuario = usuarioService.obtenerUsuarioPorEmail(emailUsuarioActual);

        // Asignar el userId al request de cambio de contraseña
        cambiarContrasenaRequest.setUsuarioId(usuario.getId());

        usuarioService.cambiarContrasena(cambiarContrasenaRequest);
        return ResponseEntity.ok("Contraseña actualizada correctamente.");
    }

    // Obtener historial de adoptantes
    @GetMapping("/historial/adoptantes")
    public ResponseEntity<List<AdoptanteResponseDto>> obtenerHistorialAdoptantes() {
        List<AdoptanteResponseDto> historial = usuarioService.obtenerHistorialAdoptantes();
        return ResponseEntity.ok(historial);
    }

    // Obtener historial de animales
    @GetMapping("/historial/animales")
    public ResponseEntity<List<AnimalResponseDto>> obtenerHistorialAnimales() {
        List<AnimalResponseDto> historial = usuarioService.obtenerHistorialAnimales();
        return ResponseEntity.ok(historial);
    }

    // Registrar un nuevo usuario
    @PostMapping
    public ResponseEntity<Usuario> registrarUsuario(@Valid @RequestBody UsuarioRequestDto usuarioDto) {
        Usuario usuario = usuarioService.guardarUsuario(usuarioDto);
        return ResponseEntity.ok(usuario);
    }

    // Actualizar un usuario existente
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioRequestDto usuarioDto) {
        Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, usuarioDto);
        return ResponseEntity.ok(usuarioActualizado);
    }

    // Eliminar un usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    // Buscar un usuario por correo electrónico
    @GetMapping("/buscarPorEmail")
    public ResponseEntity<UsuarioResponseDto> obtenerUsuarioPorEmail(@RequestParam String email) {
        Usuario usuario = usuarioService.obtenerUsuarioPorEmail(email);

        String fotoPerfilUrl = null;
        if (usuario.getFotoPerfil() != null && usuario.getFotoPerfil().length > 0) {
            String base64Image = Base64.getEncoder().encodeToString(usuario.getFotoPerfil());
            fotoPerfilUrl = "data:image/jpeg;base64," + base64Image;
        }

        UsuarioResponseDto usuarioResponse = new UsuarioResponseDto(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getDireccion(),
                new ArrayList<>(usuario.getRoles()),
                fotoPerfilUrl, fotoPerfilUrl
        );

        return ResponseEntity.ok(usuarioResponse);
    }

    // **Nuevo Endpoint para Autenticación por Nombre, Email y Contraseña**
    @PostMapping("/autenticar")
    public ResponseEntity<AutenticacionResponseDto> autenticarUsuario(@Valid @RequestBody AutenticacionRequestDto autenticacionRequest) {
        AutenticacionResponseDto response = usuarioService.autenticarYObtenerId(autenticacionRequest);
        return ResponseEntity.ok(response);
    }
}
