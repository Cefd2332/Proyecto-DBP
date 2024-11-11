package org.e2e.e2e.RegistroSalud;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;

/**
 * DTO para manejar las solicitudes de registro de salud de un animal.
 */
public class RegistroSaludRequestDto {

    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;

    @NotNull(message = "La fecha de la consulta no puede ser nula")
    @PastOrPresent(message = "La fecha de la consulta no puede ser en el futuro")
    private LocalDate fechaConsulta;

    @NotBlank(message = "El nombre del veterinario no puede estar vacío")
    private String veterinario;

    @NotNull(message = "El ID del animal no puede ser nulo")
    private Long animalId;  // ID del animal asociado al registro de salud

    // Constructores

    /**
     * Constructor por defecto.
     */
    public RegistroSaludRequestDto() {
        // Constructor vacío necesario para la deserialización
    }

    /**
     * Constructor parametrizado.
     *
     * @param descripcion    Descripción del registro de salud.
     * @param fechaConsulta  Fecha de la consulta.
     * @param veterinario    Nombre del veterinario.
     * @param animalId       ID del animal asociado.
     */
    public RegistroSaludRequestDto(String descripcion, LocalDate fechaConsulta, String veterinario, Long animalId) {
        this.descripcion = descripcion;
        this.fechaConsulta = fechaConsulta;
        this.veterinario = veterinario;
        this.animalId = animalId;
    }

    // Getters y Setters

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
     * Obtiene el ID del animal asociado al registro de salud.
     *
     * @return ID del animal.
     */
    public Long getAnimalId() {
        return animalId;
    }

    /**
     * Establece el ID del animal asociado al registro de salud.
     *
     * @param animalId ID del animal.
     */
    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }

    // Método toString para facilitar la depuración

    @Override
    public String toString() {
        return "RegistroSaludRequestDto{" +
                "descripcion='" + descripcion + '\'' +
                ", fechaConsulta=" + fechaConsulta +
                ", veterinario='" + veterinario + '\'' +
                ", animalId=" + animalId +
                '}';
    }
}
