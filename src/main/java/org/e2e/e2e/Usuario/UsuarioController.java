package org.e2e.e2e.Usuario;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> obtenerUsuarios() {
        List<UsuarioResponseDto> usuariosResponse = usuarioService.obtenerTodosLosUsuarios().stream()
                .map(usuario -> new UsuarioResponseDto(
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getEmail(),
                        usuario.getDireccion(),
                        usuario.getRoles().stream().collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuariosResponse);
    }

    // Obtener un usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> obtenerUsuarioPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);

        // Retorna el DTO sin el token, ya que no es necesario aquí
        UsuarioResponseDto usuarioResponse = new UsuarioResponseDto(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getDireccion(),
                usuario.getRoles().stream().collect(Collectors.toList())
        );

        return ResponseEntity.ok(usuarioResponse);
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
}
