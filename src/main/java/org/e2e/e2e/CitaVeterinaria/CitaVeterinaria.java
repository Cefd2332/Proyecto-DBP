package org.e2e.e2e.CitaVeterinaria;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.e2e.e2e.Animal.Animal;

import java.time.LocalDateTime;

@Data
@Entity
public class CitaVeterinaria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La fecha de la cita no puede ser nula")
    @Future(message = "La fecha de la cita debe estar en el futuro")
    private LocalDateTime fechaCita;

    @NotBlank(message = "El veterinario no puede estar vacío")
    private String veterinario;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "El estado no puede ser nulo")
    private EstadoCita estado;

    @ManyToOne
    @JoinColumn(name = "animal_id")  // Relaciona la cita con un animal
    private Animal animal;
}
