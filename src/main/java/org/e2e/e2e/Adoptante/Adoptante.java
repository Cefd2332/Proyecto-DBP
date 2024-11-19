package org.e2e.e2e.Adoptante;

import jakarta.persistence.*;
import org.e2e.e2e.Animal.Animal;
import org.e2e.e2e.Notificacion.Notificacion;
import org.e2e.e2e.Usuario.Usuario;

import java.util.*;

@Entity
@Table(name = "adoptantes")
public class Adoptante {

    @ManyToOne
    @JoinColumn(name = "usuario_id") // Asegúrate de que el nombre de la columna coincida con tu base de datos
    private Usuario usuario;


    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoAdoptante estado;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(unique = true, nullable = false)
    private String email;

    private String direccion;

    private String telefono;

    @Column(unique = true) // Asegura que cada Adoptante tenga un token único
    private String deviceToken; // Nuevo campo para el token del dispositivo

    @OneToMany(mappedBy = "adoptante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Animal> animalesAdoptados = new ArrayList<>();

    @OneToMany(mappedBy = "adoptante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notificacion> notificaciones = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "adoptante_roles", joinColumns = @JoinColumn(name = "adoptante_id"))
    @Column(name = "rol")
    private Set<String> roles = new HashSet<>();

    // Constructores

    public Adoptante() {
    }

    public Adoptante(String nombre, String email, String direccion, String telefono) {
        this.nombre = nombre;
        this.email = email;
        this.direccion = direccion;
        this.telefono = telefono;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public List<Animal> getAnimalesAdoptados() {
        return animalesAdoptados;
    }

    public void setAnimalesAdoptados(List<Animal> animalesAdoptados) {
        this.animalesAdoptados = animalesAdoptados;
    }

    public List<Notificacion> getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(List<Notificacion> notificaciones) {
        this.notificaciones = notificaciones;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    // Métodos auxiliares para manejar relaciones bidireccionales

    public void addAnimal(Animal animal) {
        animalesAdoptados.add(animal);
        animal.setAdoptante(this);
    }

    public void removeAnimal(Animal animal) {
        animalesAdoptados.remove(animal);
        animal.setAdoptante(null);
    }

    public void addNotificacion(Notificacion notificacion) {
        notificaciones.add(notificacion);
        notificacion.setAdoptante(this);
    }

    public void removeNotificacion(Notificacion notificacion) {
        notificaciones.remove(notificacion);
        notificacion.setAdoptante(null);
    }

    @PrePersist
    public void generateDeviceToken() {
        if (this.deviceToken == null || this.deviceToken.isEmpty()) {
            this.deviceToken = UUID.randomUUID().toString();
        }
    }

    // Método toString

    @Override
    public String toString() {
        return "Adoptante{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", deviceToken='" + deviceToken + '\'' +
                ", roles=" + roles +
                '}';
    }
}
