package org.e2e.e2e.Usuario;

import jakarta.persistence.*;
import lombok.Data;
import org.e2e.e2e.Animal.Animal;
import org.e2e.e2e.Notificacion.Notificacion;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String email;
    private String direccion;

    @OneToMany(mappedBy = "adoptante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Animal> animalesAdoptados = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notificacion> notificaciones = new ArrayList<>();

    // Métodos adicionales, si es necesario, serán generados por Lombok (@Data)
}
