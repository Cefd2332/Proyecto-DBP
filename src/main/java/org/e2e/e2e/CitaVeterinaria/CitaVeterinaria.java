package org.e2e.e2e.CitaVeterinaria;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.e2e.e2e.Animal.Animal;

import java.time.LocalDateTime;

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
    @JoinColumn(name = "animal_id")
    private Animal animal;

    // Constructores
    public CitaVeterinaria() {
    }

    public CitaVeterinaria(LocalDateTime fechaCita, String veterinario, EstadoCita estado, Animal animal) {
        this.fechaCita = fechaCita;
        this.veterinario = veterinario;
        this.estado = estado;
        this.animal = animal;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(LocalDateTime fechaCita) {
        this.fechaCita = fechaCita;
    }

    public String getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(String veterinario) {
        this.veterinario = veterinario;
    }

    public EstadoCita getEstado() {
        return estado;
    }

    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    @Override
    public String toString() {
        return "CitaVeterinaria{" +
                "id=" + id +
                ", fechaCita=" + fechaCita +
                ", veterinario='" + veterinario + '\'' +
                ", estado=" + estado +
                ", animalId=" + (animal != null ? animal.getId() : null) +
                '}';
    }
}
