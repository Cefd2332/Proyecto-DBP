package org.e2e.e2e.security;

import org.e2e.e2e.security.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

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
                .cors(withDefaults())  // Habilitar CORS con la configuración por defecto
                .csrf(csrf -> csrf.disable())  // Deshabilitar CSRF para API
                .authorizeHttpRequests(auth -> auth
                        // Permitir rutas de autenticación y registro sin autenticación
                        .requestMatchers("/api/auth/**").permitAll()

                        // Sólo los administradores pueden gestionar usuarios
                        .requestMatchers("/api/usuarios/**").permitAll()

                        // Los usuarios y administradores pueden adoptar animales
                        .requestMatchers("/api/adopciones/**").permitAll()

                        // Sólo los administradores pueden gestionar animales
                        .requestMatchers("/api/animales/**").permitAll()

                        // Los usuarios y administradores pueden gestionar citas
                        .requestMatchers("/api/citas/**").permitAll()

                        // Los usuarios y administradores pueden gestionar registros de salud
                        .requestMatchers("/api/registro-salud/**").permitAll()

                        // Los usuarios y administradores pueden gestionar ubicaciones
                        .requestMatchers("/api/ubicaciones/**").permitAll()

                        // Los usuarios y administradores pueden gestionar vacunas
                        .requestMatchers("/api/vacunas/**").permitAll()

                        // Los usuarios y administradores pueden gestionar notificaciones
                        .requestMatchers("/api/notificaciones/**").permitAll()

                        // Cualquier otra solicitud requiere autenticación
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        // Política de sesiones sin estado (JWT)
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        // Añadir filtro JWT antes de UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));  // Permitir todos los orígenes
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
