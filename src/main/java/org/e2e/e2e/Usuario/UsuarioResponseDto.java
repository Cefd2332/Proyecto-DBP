// src/main/java/org/e2e/e2e/Usuario/UsuarioResponseDto.java

package org.e2e.e2e.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDto {

    private Long id;
    private String nombre;
    private String email;
    private String direccion;
    private List<String> roles;        // Lista de roles del usuario
    private String fotoPerfilUrl;      // URL de la foto de perfil (si aplica)
    private String token;              // Token JWT

    // Constructor sin token y fotoPerfilUrl para respuestas comunes
    public UsuarioResponseDto(Long id, String nombre, String email, String direccion, List<String> roles) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.direccion = direccion;
        this.roles = roles;
    }
}
