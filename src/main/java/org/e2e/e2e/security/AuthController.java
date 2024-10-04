package org.e2e.e2e.security;

import org.e2e.e2e.Usuario.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/api/auth/register")
    public Usuario registrarUsuario(@RequestBody UsuarioRequestDto usuarioDto) {
        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioDto.getNombre());
        usuario.setEmail(usuarioDto.getEmail());
        usuario.setDireccion(usuarioDto.getDireccion());
        usuario.setPassword(passwordEncoder.encode(usuarioDto.getPassword()));

        // Asignar el rol USER por defecto
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");  // Añade el rol por defecto para el usuario registrado
        usuario.setRoles(roles);

        return usuarioRepository.save(usuario);
    }
}
