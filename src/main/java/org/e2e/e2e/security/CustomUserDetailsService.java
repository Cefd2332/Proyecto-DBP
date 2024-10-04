package org.e2e.e2e.security;

import org.e2e.e2e.Usuario.Role;
import org.e2e.e2e.Usuario.Usuario;
import org.e2e.e2e.Usuario.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + username));

        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(),
                usuario.getPassword(),
                mapRolesToAuthorities(usuario.getRoles())  // Se debe manejar Set<String> para los roles
        );
    }

    // Mapear los roles a GrantedAuthority
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<String> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role))  // SimpleGrantedAuthority mapea el rol
                .collect(Collectors.toSet());  // Se usa Set para evitar duplicados
    }
}
