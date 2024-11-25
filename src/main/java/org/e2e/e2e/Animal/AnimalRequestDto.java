package org.e2e.e2e.Animal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * DTO para recibir solicitudes relacionadas con la entidad Animal.
 */
public class AnimalRequestDto {

    @NotBlank(message = "El nombre del animal no puede estar vacío")
    private String nombre;

    @NotBlank(message = "La especie no puede estar vacía")
    private String especie;

    @Min(value = 0, message = "La edad debe ser un valor positivo")
    private int edad;

    @NotBlank(message = "La unidad de edad no puede estar vacía")
    private String unidadEdad;

    @NotBlank(message = "El estado de salud no puede estar vacío")
    private String estadoSalud;

    /**
     * Fecha de adopción del animal.
     * Este campo es opcional en la creación, pero puede ser útil para actualizaciones.
     */
    private LocalDate fechaAdopcion;

    /**
     * Estado actual del animal.
     * Este campo es opcional en la creación, ya que el servicio establece el estado inicial.
     */
    private EstadoAnimal estadoActual;  // Este campo es opcional, puede omitirse si no es necesario

    @NotNull(message = "El adoptante no puede ser nulo")
    private Long adoptanteId;

    @NotNull(message = "El género no puede estar vacío")
    private GeneroAnimal genero;

    // Constructores

    /**
     * Constructor por defecto.
     */
    public AnimalRequestDto() {
        // Constructor vacío necesario para la deserialización
    }

    /**
     * Constructor parametrizado para facilitar la creación de instancias.
     *
     * @param nombre        Nombre del animal.
     * @param especie       Especie del animal.
     * @param edad          Edad del animal.
     * @param unidadEdad    Unidad de la edad del animal (años/meses).
     * @param estadoSalud   Estado de salud del animal.
     * @param fechaAdopcion Fecha de adopción del animal.
     * @param estadoActual  Estado actual del animal.
     * @param adoptanteId   ID del adoptante.
     * @param genero        Género del animal.
     */
    public AnimalRequestDto(String nombre, String especie, int edad, String unidadEdad, String estadoSalud,
                            LocalDate fechaAdopcion, EstadoAnimal estadoActual, Long adoptanteId, GeneroAnimal genero) {
        this.nombre = nombre;
        this.especie = especie;
        this.edad = edad;
        this.unidadEdad = unidadEdad;
        this.estadoSalud = estadoSalud;
        this.fechaAdopcion = fechaAdopcion;
        this.estadoActual = estadoActual;
        this.adoptanteId = adoptanteId;
        this.genero = genero; // Nuevo campo
    }

    // Getters y Setters

    public GeneroAnimal getGenero() {
        return genero;
    }

    public void setGenero(GeneroAnimal genero) {
        this.genero = genero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getUnidadEdad() {
        return unidadEdad;
    }

    public void setUnidadEdad(String unidadEdad) {
        this.unidadEdad = unidadEdad;
    }

    public String getEstadoSalud() {
        return estadoSalud;
    }

    public void setEstadoSalud(String estadoSalud) {
        this.estadoSalud = estadoSalud;
    }

    public LocalDate getFechaAdopcion() {
        return fechaAdopcion;
    }

    public void setFechaAdopcion(LocalDate fechaAdopcion) {
        this.fechaAdopcion = fechaAdopcion;
    }

    public EstadoAnimal getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(EstadoAnimal estadoActual) {
        this.estadoActual = estadoActual;
    }

    public Long getAdoptanteId() {
        return adoptanteId;
    }

    public void setAdoptanteId(Long adoptanteId) {
        this.adoptanteId = adoptanteId;
    }

    // Métodos auxiliares (si es necesario)

    @Override
    public String toString() {
        return "AnimalRequestDto{" +
                "nombre='" + nombre + '\'' +
                ", especie='" + especie + '\'' +
                ", edad=" + edad +
                ", unidadEdad='" + unidadEdad + '\'' +
                ", estadoSalud='" + estadoSalud + '\'' +
                ", fechaAdopcion=" + fechaAdopcion +
                ", estadoActual=" + estadoActual +
                ", adoptanteId=" + adoptanteId +
                ", genero=" + genero +
                '}';
    }
}
