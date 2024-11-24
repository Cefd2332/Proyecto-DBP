package org.e2e.e2e.Usuario;

import jakarta.persistence.*;
import org.e2e.e2e.Notificacion.Notificacion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(unique = true, nullable = false)
    private String email;

    private String direccion;

    private String password;

    private String token;

    @Lob
    private byte[] fotoPerfil;


    // Se elimina la relación con animalesAdoptados
    // @OneToMany(mappedBy = "adoptante", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<Animal> animalesAdoptados = new ArrayList<>();

    // Se elimina la relación con notificaciones
    // @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<Notificacion> notificaciones = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "usuario_roles", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "rol")
    private Set<String> roles = new HashSet<>();

    // Constructores
    public Usuario() {
    }

    public Usuario(String nombre, String email, String direccion, String password, String token) {
        this.nombre = nombre;
        this.email = email;
        this.direccion = direccion;
        this.password = password;
        this.token = token;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

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

    public byte[] getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(byte[] fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    // Método toString
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", direccion='" + direccion + '\'' +
                ", token='" + token + '\'' +
                ", roles=" + roles +
                '}';
    }
}
