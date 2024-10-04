package org.e2e.e2e.Usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class UsuarioRequestDto {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    private String email;

    @NotBlank(message = "La dirección no puede estar vacía")
    @Size(max = 100, message = "La dirección no debe exceder los 100 caracteres")
    private String direccion;

    // Campo de contraseña con validaciones
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 20, message = "La contraseña debe tener entre 8 y 20 caracteres")
    private String password;

    // Campo de confirmación de contraseña para validación
    @NotBlank(message = "Debe confirmar la contraseña")
    @Size(min = 8, max = 20, message = "La confirmación de la contraseña debe tener entre 8 y 20 caracteres")
    private String confirmPassword;

    // Campo opcional para token de notificaciones push
    private String token;

    // Campo para recibir los roles del usuario (ADMIN, USER, etc.)
    private Set<String> roles;

    // Método para verificar si las contraseñas coinciden (útil en validaciones manuales)
    public boolean isPasswordConfirmed() {
        return password != null && password.equals(confirmPassword);
    }
}
