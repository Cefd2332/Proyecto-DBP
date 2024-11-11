package org.e2e.e2e.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;  // La clave secreta almacenada en application.properties o application.yml

    @Value("${jwt.expiration}")
    private long expiration;  // Tiempo de expiración del token (milisegundos)

    // Crear la clave para firmar el token a partir del secreto
    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());
    }

    // Generar un token JWT a partir de los detalles del usuario
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        // Convertir GrantedAuthority a List<String> para almacenar en el token
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", roles);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date()) // Fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Fecha de expiración
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // Validar si el token es correcto (no ha expirado y la firma es válida)
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String username = getUsernameFromToken(token);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (SignatureException e) {
            // Firma del token no válida
            System.out.println("Invalid JWT signature: " + e.getMessage());
            return false;
        } catch (ExpiredJwtException e) {
            // Token expirado
            System.out.println("JWT token is expired: " + e.getMessage());
            return false;
        } catch (Exception e) {
            // Otros errores
            System.out.println("Invalid JWT token: " + e.getMessage());
            return false;
        }
    }

    // Obtener el nombre de usuario contenido en el token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);  // El 'subject' es el username
    }

    // Verificar si el token ha expirado
    private boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    // Obtener la fecha de expiración del token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);  // El 'exp' es la fecha de expiración
    }

    // Obtener un 'claim' específico del token JWT
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .setAllowedClockSkewSeconds(60)  // Permite 60 segundos de diferencia en el tiempo
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    // Extraer el token JWT del encabezado de la solicitud HTTP
    public String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);  // Quitar "Bearer " del inicio del token
        }
        return null;
    }

    // Establecer el contexto de seguridad para el usuario autenticado
    public static void setSecurityContext(UserDetails userDetails, HttpServletRequest request) {
        var auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
