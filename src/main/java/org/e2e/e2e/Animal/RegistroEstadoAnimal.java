package org.e2e.e2e.Animal;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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

    // Constructores
    public RegistroEstadoAnimal() {
    }

    public RegistroEstadoAnimal(EstadoAnimal estado, LocalDateTime fechaCambio, Animal animal) {
        this.estado = estado;
        this.fechaCambio = fechaCambio;
        this.animal = animal;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EstadoAnimal getEstado() {
        return estado;
    }

    public void setEstado(EstadoAnimal estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(LocalDateTime fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    @Override
    public String toString() {
        return "RegistroEstadoAnimal{" +
                "id=" + id +
                ", estado=" + estado +
                ", fechaCambio=" + fechaCambio +
                ", animalId=" + (animal != null ? animal.getId() : null) +
                '}';
    }
}
