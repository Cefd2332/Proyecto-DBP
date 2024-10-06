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
    private String token;  // El token JWT

    // Constructor sin token para las respuestas comunes
    public UsuarioResponseDto(Long id, String nombre, String email, String direccion, List<String> roles) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.direccion = direccion;
    }

    // Constructor con token solo cuando sea necesario (por ejemplo en autenticación)
    public UsuarioResponseDto(Long id, String nombre, String email, String direccion, String token, List<String> roles) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.direccion = direccion;
        this.token = token;
    }
}
