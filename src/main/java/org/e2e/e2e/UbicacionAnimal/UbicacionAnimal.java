package org.e2e.e2e.UbicacionAnimal;

import jakarta.persistence.*;
import lombok.Data;
import org.e2e.e2e.Animal.Animal;

import java.time.LocalDateTime;

@Data
@Entity
public class UbicacionAnimal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double latitud;
    private double longitud;
    private LocalDateTime fechaHora;  // Momento en que se registró la ubicación

    @ManyToOne
    @JoinColumn(name = "animal_id")  // Relaciona la ubicación con un animal
    private Animal animal;
}
