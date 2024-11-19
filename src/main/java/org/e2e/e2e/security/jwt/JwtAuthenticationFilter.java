package org.e2e.e2e.security.jwt;

import org.e2e.e2e.security.CustomUserDetailsService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, CustomUserDetailsService customUserDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.customUserDetailsService = customUserDetailsService;
    }
    public String obtenerEmailDelToken() {
        // Obtén el principal del SecurityContext (generalmente el email o nombre de usuario)
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        try {
            // Extraer el token del encabezado de la solicitud
            String token = jwtTokenUtil.getTokenFromRequest(request);

            // Validar el token
            if (token != null) {
                // Extraer el nombre de usuario del token
                String username = jwtTokenUtil.getUsernameFromToken(token);

                // Cargar detalles del usuario desde el servicio de UserDetails
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                // Validar el token comparándolo con los detalles del usuario
                if (jwtTokenUtil.validateToken(token, userDetails)) {
                    // Si el contexto de seguridad está vacío, establecer la autenticación
                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        // Configurar el contexto de seguridad con el usuario autenticado
                        JwtTokenUtil.setSecurityContext(userDetails, request);
                    }
                }
            }
        } catch (Exception ex) {
            // Manejo de excepciones (opcionalmente loguear)
            System.out.println("Error durante la validación del token JWT: " + ex.getMessage());
        }

        // Continuar con el filtro
        chain.doFilter(request, response);
    }
}
