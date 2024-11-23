package org.e2e.e2e.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    // Si ya no necesitas el filtro JWT, puedes eliminar esta línea y el método que lo agrega.
    // private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Constructor
    /*
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
    */

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
                        // Permitir todas las solicitudes sin autenticación
                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session
                        // Política de sesiones sin estado (JWT), aunque no se use en esta configuración
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        /*
        // Si decides mantener el filtro JWT, pero todas las solicitudes son permitidas,
        // puedes mantener esta línea, aunque el filtro no tendrá efecto práctico.
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        */

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permitir cualquier origen
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        // Permitir métodos HTTP específicos
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        // Permitir encabezados específicos
        configuration.setAllowedHeaders(Arrays.asList("*"));
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
