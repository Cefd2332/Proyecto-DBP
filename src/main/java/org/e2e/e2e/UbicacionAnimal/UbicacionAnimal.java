package org.e2e.e2e.UbicacionAnimal;

import jakarta.persistence.*;
import org.e2e.e2e.Animal.Animal;
import java.time.LocalDateTime;

@Entity
public class UbicacionAnimal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double latitud;
    private double longitud;
    private LocalDateTime fechaHora;

    @ManyToOne
    @JoinColumn(name = "animal_id")
    private Animal animal;

    // Constructores
    public UbicacionAnimal() {
    }

    public UbicacionAnimal(double latitud, double longitud, LocalDateTime fechaHora, Animal animal) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaHora = fechaHora;
        this.animal = animal;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    @Override
    public String toString() {
        return "UbicacionAnimal{" +
                "id=" + id +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", fechaHora=" + fechaHora +
                ", animalId=" + (animal != null ? animal.getId() : null) +
                '}';
    }
}
