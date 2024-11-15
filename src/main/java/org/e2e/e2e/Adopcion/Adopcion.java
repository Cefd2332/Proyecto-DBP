package org.e2e.e2e.Adopcion;

import jakarta.persistence.*;
import org.e2e.e2e.Adoptante.Adoptante;
import org.e2e.e2e.Animal.Animal;

import java.time.LocalDate;

/**
 * Entidad que representa la adopción de un animal.
 */
@Entity
@Table(name = "adopciones")
public class Adopcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fechaAdopcion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "adoptante_id", nullable = false)
    private Adoptante adoptante;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "animal_id", nullable = false, unique = true)
    private Animal animal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoAdopcion estado;

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
     * @param adoptante     Adoptante que realiza la adopción.
     * @param animal        Animal adoptado.
     * @param estado        Estado de la adopción.
     */
    public Adopcion(LocalDate fechaAdopcion, Adoptante adoptante, Animal animal, EstadoAdopcion estado) {
        this.fechaAdopcion = fechaAdopcion;
        this.adoptante = adoptante;
        this.animal = animal;
        this.estado = estado;
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    // No se proporciona setId ya que generalmente el ID es generado automáticamente

    public LocalDate getFechaAdopcion() {
        return fechaAdopcion;
    }

    public void setFechaAdopcion(LocalDate fechaAdopcion) {
        this.fechaAdopcion = fechaAdopcion;
    }

    public Adoptante getAdoptante() {
        return adoptante;
    }

    public void setAdoptante(Adoptante adoptante) {
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
