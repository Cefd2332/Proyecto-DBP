package org.e2e.e2e.Adopcion;

import jakarta.persistence.*;
import org.e2e.e2e.Animal.Animal;
import org.e2e.e2e.Usuario.Usuario;

import java.time.LocalDate;

/**
 * Entidad que representa la adopción de un animal.
 */
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

    @Enumerated(EnumType.STRING)  // Enumera el estado de la adopción
    private EstadoAdopcion estado;  // Estado de la adopción

    // Constructores

    /**
     * Constructor por defecto.
     */
    public Adopcion() {
        // Constructor vacío necesario para la deserialización
    }

    /**
     * Constructor parametrizado.
     *
     * @param fechaAdopcion Fecha de la adopción.
     * @param adoptante     Usuario que adopta el animal.
     * @param animal        Animal adoptado.
     * @param estado        Estado de la adopción.
     */
    public Adopcion(LocalDate fechaAdopcion, Usuario adoptante, Animal animal, EstadoAdopcion estado) {
        this.fechaAdopcion = fechaAdopcion;
        this.adoptante = adoptante;
        this.animal = animal;
        this.estado = estado;
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaAdopcion() {
        return fechaAdopcion;
    }

    public void setFechaAdopcion(LocalDate fechaAdopcion) {
        this.fechaAdopcion = fechaAdopcion;
    }

    public Usuario getAdoptante() {
        return adoptante;
    }

    public void setAdoptante(Usuario adoptante) {
        this.adoptante = adoptante;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public EstadoAdopcion getEstado() {
        return estado;
    }

    public void setEstado(EstadoAdopcion estado) {
        this.estado = estado;
    }

    // Método toString para facilitar la depuración

    @Override
    public String toString() {
        return "Adopcion{" +
                "id=" + id +
                ", fechaAdopcion=" + fechaAdopcion +
                ", adoptanteId=" + (adoptante != null ? adoptante.getId() : null) +
                ", animalId=" + (animal != null ? animal.getId() : null) +
                ", estado=" + estado +
                '}';
    }
}
