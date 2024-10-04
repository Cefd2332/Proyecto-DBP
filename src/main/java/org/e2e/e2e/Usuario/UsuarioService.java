package org.e2e.e2e.Usuario;

import lombok.RequiredArgsConstructor;
import org.e2e.e2e.exceptions.NotFoundException;  // Importa la excepción personalizada
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

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
        return usuarioRepository.save(usuario);
    }

    // Actualizar un usuario existente
    public Usuario actualizarUsuario(Long id, UsuarioRequestDto usuarioDto) {
        Usuario usuario = obtenerUsuarioPorId(id);  // Lanza excepción si no se encuentra
        usuario.setNombre(usuarioDto.getNombre());
        usuario.setEmail(usuarioDto.getEmail());
        usuario.setDireccion(usuarioDto.getDireccion());
        return usuarioRepository.save(usuario);
    }

    // Eliminar un usuario por ID
    public void eliminarUsuario(Long id) {
        // Verificar si el usuario existe antes de eliminar
        if (!usuarioRepository.existsById(id)) {
            throw new NotFoundException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}
