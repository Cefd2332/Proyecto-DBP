package org.e2e.e2e.CitaVeterinaria;

import jakarta.persistence.*;
import lombok.Data;
import org.e2e.e2e.Animal.Animal;

import java.time.LocalDateTime;

@Data
@Entity
public class CitaVeterinaria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fechaCita;
    private String veterinario;
    private String estado; // Ejemplo: "pendiente", "realizada", "cancelada"

    @ManyToOne
    @JoinColumn(name = "animal_id")  // Relaciona la cita con un animal
    private Animal animal;
}
