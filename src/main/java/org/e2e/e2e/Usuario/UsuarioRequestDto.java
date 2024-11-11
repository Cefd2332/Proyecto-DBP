package org.e2e.e2e.Usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

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

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 20, message = "La contraseña debe tener entre 8 y 20 caracteres")
    private String password;

    @NotBlank(message = "Debe confirmar la contraseña")
    @Size(min = 8, max = 20, message = "La confirmación de la contraseña debe tener entre 8 y 20 caracteres")
    private String confirmPassword;

    private String token;

    private Set<String> roles;

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    // Método para verificar si las contraseñas coinciden
    public boolean isPasswordConfirmed() {
        return password != null && password.equals(confirmPassword);
    }

    @Override
    public String toString() {
        return "UsuarioRequestDto{" +
                "nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", direccion='" + direccion + '\'' +
                ", token='" + token + '\'' +
                ", roles=" + roles +
                '}';
    }
}
