package org.e2e.e2e.Vacuna;

import jakarta.persistence.*;
import org.e2e.e2e.Animal.Animal;
import java.time.LocalDate;

@Entity
public class Vacuna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private LocalDate fechaAplicacion;

    @ManyToOne
    @JoinColumn(name = "animal_id")
    private Animal animal;

    // Constructores
    public Vacuna() {
    }

    public Vacuna(String nombre, LocalDate fechaAplicacion, Animal animal) {
        this.nombre = nombre;
        this.fechaAplicacion = fechaAplicacion;
        this.animal = animal;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(LocalDate fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    @Override
    public String toString() {
        return "Vacuna{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", fechaAplicacion=" + fechaAplicacion +
                ", animalId=" + (animal != null ? animal.getId() : null) +
                '}';
    }
}
