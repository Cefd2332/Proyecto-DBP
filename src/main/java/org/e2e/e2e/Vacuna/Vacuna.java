package org.e2e.e2e.Vacuna;

import jakarta.persistence.*;
import lombok.Data;
import org.e2e.e2e.Animal.Animal;

import java.time.LocalDate;

@Data
@Entity
public class Vacuna {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private LocalDate fechaAplicacion;

    @ManyToOne
    @JoinColumn(name = "animal_id")  // Relaciona la vacuna con un animal
    private Animal animal;
}
