package org.e2e.e2e.Adopcion;

import jakarta.persistence.*;
import lombok.Data;
import org.e2e.e2e.Animal.Animal;
import org.e2e.e2e.Usuario.Usuario;

import java.time.LocalDate;

@Data
@Entity
public class Adopcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaAdopcion;

    @ManyToOne
    @JoinColumn(name = "usuario_id")  // Relaciona la adopción con el adoptante
    private Usuario adoptante;

    @OneToOne
    @JoinColumn(name = "animal_id")  // Relaciona la adopción con el animal
    private Animal animal;
}
