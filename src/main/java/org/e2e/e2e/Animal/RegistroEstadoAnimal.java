package org.e2e.e2e.Animal;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class RegistroEstadoAnimal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EstadoAnimal estado;

    private LocalDateTime fechaCambio;

    @ManyToOne
    @JoinColumn(name = "animal_id")
    private Animal animal;
}
