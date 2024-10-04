package org.e2e.e2e.security;

import lombok.RequiredArgsConstructor;
import org.e2e.e2e.security.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Deshabilitar CSRF para API
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()  // Autenticación y registro permitidos para todos
                        .requestMatchers("/api/usuarios/**").hasRole("ADMIN")  // Solo ADMIN puede gestionar usuarios
                        .requestMatchers("/api/adopciones/**").hasAnyRole("USER", "ADMIN")  // USER y ADMIN pueden adoptar animales
                        .requestMatchers("/api/animales/**").hasRole("ADMIN")  // Solo ADMIN puede gestionar animales
                        .requestMatchers("/api/citas/**").hasAnyRole("USER", "ADMIN")  // USER y ADMIN gestionan citas
                        .requestMatchers("/api/registro-salud/**").hasAnyRole("USER", "ADMIN")  // USER y ADMIN gestionan registros de salud
                        .requestMatchers("/api/ubicaciones/**").hasAnyRole("USER", "ADMIN")  // USER y ADMIN gestionan ubicaciones
                        .requestMatchers("/api/vacunas/**").hasAnyRole("USER", "ADMIN")  // USER y ADMIN gestionan vacunas
                        .requestMatchers("/api/notificaciones/**").hasAnyRole("USER", "ADMIN")  // USER y ADMIN gestionan notificaciones
                        .anyRequest().authenticated()  // Todo lo demás requiere autenticación
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // Política de sesiones sin estado (JWT)
                );

        // Añadir filtro JWT antes de UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
