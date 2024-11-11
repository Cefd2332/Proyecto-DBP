package org.e2e.e2e.RegistroSalud;

import jakarta.persistence.*;
import org.e2e.e2e.Animal.Animal;

import java.time.LocalDate;

/**
 * Entidad que representa un registro de salud de un animal.
 */
@Entity
public class RegistroSalud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;

    private LocalDate fechaConsulta;

    private String veterinario;

    @ManyToOne
    @JoinColumn(name = "animal_id")  // Foreign key reference to Animal
    private Animal animal;

    // Constructores

    /**
     * Constructor por defecto.
     */
    public RegistroSalud() {
        // Constructor vacío necesario para la deserialización
    }

    /**
     * Constructor parametrizado.
     *
     * @param descripcion    Descripción del registro de salud.
     * @param fechaConsulta  Fecha de la consulta.
     * @param veterinario    Nombre del veterinario.
     * @param animal         Animal asociado al registro de salud.
     */
    public RegistroSalud(String descripcion, LocalDate fechaConsulta, String veterinario, Animal animal) {
        this.descripcion = descripcion;
        this.fechaConsulta = fechaConsulta;
        this.veterinario = veterinario;
        this.animal = animal;
    }

    // Getters y Setters

    /**
     * Obtiene el ID del registro de salud.
     *
     * @return ID del registro.
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el ID del registro de salud.
     * <p><strong>Nota:</strong> Generalmente no se utiliza este método ya que el ID es generado automáticamente.</p>
     *
     * @param id ID del registro.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene la descripción del registro de salud.
     *
     * @return Descripción del registro.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción del registro de salud.
     *
     * @param descripcion Descripción del registro.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene la fecha de la consulta.
     *
     * @return Fecha de la consulta.
     */
    public LocalDate getFechaConsulta() {
        return fechaConsulta;
    }

    /**
     * Establece la fecha de la consulta.
     *
     * @param fechaConsulta Fecha de la consulta.
     */
    public void setFechaConsulta(LocalDate fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    /**
     * Obtiene el nombre del veterinario.
     *
     * @return Nombre del veterinario.
     */
    public String getVeterinario() {
        return veterinario;
    }

    /**
     * Establece el nombre del veterinario.
     *
     * @param veterinario Nombre del veterinario.
     */
    public void setVeterinario(String veterinario) {
        this.veterinario = veterinario;
    }

    /**
     * Obtiene el animal asociado al registro de salud.
     *
     * @return Animal asociado.
     */
    public Animal getAnimal() {
        return animal;
    }

    /**
     * Establece el animal asociado al registro de salud.
     *
     * @param animal Animal asociado.
     */
    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    // Método toString para facilitar la depuración

    @Override
    public String toString() {
        return "RegistroSalud{" +
                "id=" + id +
                ", descripcion='" + descripcion + '\'' +
                ", fechaConsulta=" + fechaConsulta +
                ", veterinario='" + veterinario + '\'' +
                ", animalId=" + (animal != null ? animal.getId() : null) +
                '}';
    }
}
