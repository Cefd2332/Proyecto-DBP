package org.e2e.e2e.Adoptante;

import java.util.List;

public class AdoptanteResponseDto {

    private Long id;
    private String nombre;
    private String email;
    private String direccion;
    private String telefono;
    private List<String> roles;
    private String deviceToken; // Nuevo campo añadido

    // Constructor para funcion Perfil
    public AdoptanteResponseDto(Long id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }

    // Constructor actualizado
    public AdoptanteResponseDto(Long id, String nombre, String email, String direccion, String telefono, List<String> roles, String deviceToken) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.direccion = direccion;
        this.telefono = telefono;
        this.roles = roles;
        this.deviceToken = deviceToken;
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public List<String> getRoles() {
        return roles;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
