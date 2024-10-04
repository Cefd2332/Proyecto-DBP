package org.e2e.e2e.Usuario;

import jakarta.persistence.*;
import lombok.Data;
import org.e2e.e2e.Animal.Animal;
import org.e2e.e2e.Notificacion.Notificacion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "usuarios") // Se puede definir el nombre de la tabla explícitamente
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(unique = true, nullable = false)
    private String email;

    private String direccion;

    private String password;

    // Campo para almacenar el token FCM para notificaciones push
    private String token;

    @OneToMany(mappedBy = "adoptante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Animal> animalesAdoptados = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notificacion> notificaciones = new ArrayList<>();

    // Relación para almacenar los roles de usuario (muchos a muchos)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "usuario_roles", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "rol")
    private Set<String> roles = new HashSet<>();  // Los roles pueden ser "ROLE_USER", "ROLE_ADMIN", etc.

    
}
