package org.e2e.e2e.RegistroMedico;

import jakarta.persistence.*;
import lombok.Data;
import org.e2e.e2e.Animal.Animal;

import java.time.LocalDate;

@Data
@Entity
public class RegistroSalud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;
    private LocalDate fechaConsulta;
    private String veterinario;

    @ManyToOne
    @JoinColumn(name = "animal_id")  // Foreign key reference to Animal
    private Animal animal;


}
