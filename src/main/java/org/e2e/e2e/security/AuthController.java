package org.e2e.e2e.security;

import org.e2e.e2e.Usuario.*;
import org.e2e.e2e.security.jwt.JwtTokenUtil;  // Importar la clase para generar el token
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.scheduling.annotation.Async;

import jakarta.validation.Valid;

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
    private JwtTokenUtil jwtTokenUtil;  // Inyectar la utilidad para manejar el JWT

    @PostMapping("/register")
    @Async  // Hacer asíncrono el registro
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
        roles.add("ROLE_USER");  // Añade el rol por defecto para el usuario registrado
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
                    token
            );
            return ResponseEntity.ok(responseDto);
        });
    }

    // Método auxiliar para crear UserDetails a partir del usuario de forma asíncrona
    @Async
    public CompletableFuture<UserDetails> createUserDetailsAsync(Usuario usuario) {
        return CompletableFuture.completedFuture(new org.springframework.security.core.userdetails.User(
                usuario.getEmail(),
                usuario.getPassword(),
                mapRolesToAuthorities(usuario.getRoles())  // Convertir roles a GrantedAuthority
        ));
    }

    // Método auxiliar para convertir roles de String a GrantedAuthority
    private Set<GrantedAuthority> mapRolesToAuthorities(Set<String> roles) {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)  // Crear una nueva instancia de SimpleGrantedAuthority
                .collect(Collectors.toSet());
    }


    @PostMapping("/login")
    public CompletableFuture<ResponseEntity<UsuarioResponseDto>> login(@Valid @RequestBody LoginRequestDto loginDto) {
        return usuarioRepository.findByEmail(loginDto.getEmail())
                .thenApplyAsync(optionalUser -> {
                    if (optionalUser.isPresent()) {
                        Usuario usuario = optionalUser.get();
                        if (passwordEncoder.matches(loginDto.getPassword(), usuario.getPassword())) {
                            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
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
                                    token
                            );
                            return ResponseEntity.ok(responseDto);
                        } else {
                            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
                        }
                    } else {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
                    }
                });
}}
