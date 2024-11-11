package org.e2e.e2e.Animal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AnimalRequestDto {

    @NotBlank(message = "El nombre del animal no puede estar vacío")
    private String nombre;

    @NotBlank(message = "La especie no puede estar vacía")
    private String especie;

    @Min(value = 0, message = "La edad debe ser un valor positivo")
    private int edad;

    @NotBlank(message = "El estado de salud no puede estar vacío")
    private String estadoSalud;

    @NotNull(message = "El adoptante no puede ser nulo")
    private Long adoptanteId;

    // Nuevo campo para controlar el estado actual del animal
    private EstadoAnimal estado;  // Este campo es opcional, puede omitirse si no es necesario

    // Constructores

    /**
     * Constructor por defecto.
     */
    public AnimalRequestDto() {
        // Constructor vacío necesario para la deserialización
    }

    /**
     * Constructor parametrizado.
     *
     * @param nombre        Nombre del animal.
     * @param especie       Especie del animal.
     * @param edad          Edad del animal.
     * @param estadoSalud   Estado de salud del animal.
     * @param adoptanteId   ID del adoptante.
     * @param estado        Estado actual del animal.
     */
    public AnimalRequestDto(String nombre, String especie, int edad, String estadoSalud, Long adoptanteId, EstadoAnimal estado) {
        this.nombre = nombre;
        this.especie = especie;
        this.edad = edad;
        this.estadoSalud = estadoSalud;
        this.adoptanteId = adoptanteId;
        this.estado = estado;
    }

    // Getters y Setters

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

    public String getEstadoSalud() {
        return estadoSalud;
    }

    public void setEstadoSalud(String estadoSalud) {
        this.estadoSalud = estadoSalud;
    }

    public Long getAdoptanteId() {
        return adoptanteId;
    }

    public void setAdoptanteId(Long adoptanteId) {
        this.adoptanteId = adoptanteId;
    }

    public EstadoAnimal getEstado() {
        return estado;
    }

    public void setEstado(EstadoAnimal estado) {
        this.estado = estado;
    }

    // Métodos auxiliares (si es necesario)

    @Override
    public String toString() {
        return "AnimalRequestDto{" +
                "nombre='" + nombre + '\'' +
                ", especie='" + especie + '\'' +
                ", edad=" + edad +
                ", estadoSalud='" + estadoSalud + '\'' +
                ", adoptanteId=" + adoptanteId +
                ", estado=" + estado +
                '}';
    }
}
