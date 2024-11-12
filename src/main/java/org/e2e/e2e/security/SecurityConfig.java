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
                .cors(withDefaults())  // Habilitar CORS con la configuración definida en CorsConfigurationSource
                .csrf(csrf -> csrf.disable())  // Deshabilitar CSRF para API
                .authorizeHttpRequests(auth -> auth
                        // Permitir rutas de autenticación y registro sin autenticación
                        .requestMatchers("/api/auth/**").permitAll()

                        // Permitir todas las rutas de otros endpoints (puedes ajustar según tus necesidades)
                        .requestMatchers("/api/usuarios/**").permitAll()
                        .requestMatchers("/api/adopciones/**").permitAll()
                        .requestMatchers("/api/animales/**").permitAll()
                        .requestMatchers("/api/citas/**").permitAll()
                        .requestMatchers("/api/registro-salud/**").permitAll()
                        .requestMatchers("/api/ubicaciones/**").permitAll()
                        .requestMatchers("/api/vacunas/**").permitAll()
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
        // Especificar el origen permitido (React frontend)
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        // Permitir métodos HTTP específicos
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        // Permitir encabezados específicos
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        // Permitir credenciales
        configuration.setAllowCredentials(true);
        // Opcional: establecer el tiempo de vida de la configuración CORS
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplicar la configuración a todas las rutas
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
