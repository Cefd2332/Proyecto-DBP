package org.e2e.e2e.Animal;

import jakarta.persistence.*;
import lombok.Data;
import org.e2e.e2e.CitaVeterinaria.CitaVeterinaria;
import org.e2e.e2e.RegistroSalud.RegistroSalud;
import org.e2e.e2e.UbicacionAnimal.UbicacionAnimal;
import org.e2e.e2e.Usuario.Usuario;
import org.e2e.e2e.Vacuna.Vacuna;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String especie;
    private int edad;
    private String estadoSalud;
    private LocalDate fechaAdopcion;

    @Enumerated(EnumType.STRING)
    private EstadoAnimal estadoActual;  // Estado actual del animal

    @ManyToOne
    @JoinColumn(name = "adoptante_id")
    private Usuario adoptante;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistroEstadoAnimal> registroEstadoAnimal = new ArrayList<>();

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistroEstadoAnimal> historialEstados = new ArrayList<>();

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistroSalud> historialMedico = new ArrayList<>();

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CitaVeterinaria> citasVeterinarias = new ArrayList<>();

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UbicacionAnimal> ubicaciones = new ArrayList<>();

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vacuna> vacunas = new ArrayList<>();


}
