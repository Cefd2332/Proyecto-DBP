package org.e2e.e2e.security;



import org.e2e.e2e.Usuario.*;
import org.e2e.e2e.security.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.scheduling.annotation.Async;

import jakarta.validation.Valid;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/register")
    @Async
    public CompletableFuture<ResponseEntity<UsuarioResponseDto>> registrarUsuario(@Valid @RequestBody UsuarioRequestDto usuarioDto) {
        // Verificar si el usuario ya existe por su email
        if (usuarioRepository.findByEmail(usuarioDto.getEmail()).isPresent()) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body(null));  // Retornar error si el usuario ya existe
        }

        // Crear una nueva instancia de Usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioDto.getNombre());
        usuario.setEmail(usuarioDto.getEmail());
        usuario.setDireccion(usuarioDto.getDireccion());
        usuario.setPassword(passwordEncoder.encode(usuarioDto.getPassword()));  // Codificar la contraseña

        // Asignar el rol USER por defecto
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");
        usuario.setRoles(roles);

        // Guardar el usuario en la base de datos
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        // Generar el token JWT para el usuario guardado de manera asíncrona
        CompletableFuture<UserDetails> userDetailsFuture = createUserDetailsAsync(usuarioGuardado);

        return userDetailsFuture.thenApply(userDetails -> {
            // Generar el token JWT con el UserDetails
            String token = jwtTokenUtil.generateToken(userDetails);

            // Retornar el usuario y el token en la respuesta
            UsuarioResponseDto responseDto = new UsuarioResponseDto(
                    usuarioGuardado.getId(),
                    usuarioGuardado.getNombre(),
                    usuarioGuardado.getEmail(),
                    usuarioGuardado.getDireccion(),
                    Collections.singletonList(token)
            );
            return ResponseEntity.ok(responseDto);
        });
    }

    // Método auxiliar para crear UserDetails a partir del usuario de forma asíncrona
    @Async
    public CompletableFuture<UserDetails> createUserDetailsAsync(Usuario usuario) {
        return CompletableFuture.completedFuture(new User(
                usuario.getEmail(),
                usuario.getPassword(),
                mapRolesToAuthorities(usuario.getRoles())
        ));
    }

    // Método auxiliar para convertir roles de String a GrantedAuthority
    private Set<GrantedAuthority> mapRolesToAuthorities(Set<String> roles) {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @PostMapping("/login")
    @Async
    public CompletableFuture<ResponseEntity<UsuarioResponseDto>> login(@Valid @RequestBody LoginRequestDto loginDto) {
        return CompletableFuture.supplyAsync(() -> {
            return (ResponseEntity<UsuarioResponseDto>) usuarioRepository.findByEmail(loginDto.getEmail())
                    .map(usuario -> {
                        if (passwordEncoder.matches(loginDto.getPassword(), usuario.getPassword())) {
                            UserDetails userDetails = new User(
                                    usuario.getEmail(),
                                    usuario.getPassword(),
                                    mapRolesToAuthorities(usuario.getRoles())
                            );
                            String token = jwtTokenUtil.generateToken(userDetails);
                            UsuarioResponseDto responseDto = new UsuarioResponseDto(
                                    usuario.getId(),
                                    usuario.getNombre(),
                                    usuario.getEmail(),
                                    usuario.getDireccion(),
                                    Collections.singletonList(token)
                            );
                            return ResponseEntity.ok(responseDto);
                        } else {
                            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
                        }
                    })
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null));
        });
    }
}
