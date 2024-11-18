package org.e2e.e2e.Animal;

public class AnimalResponseDto {

    private Long id;
    private String nombre;
    private String especie;
    private int edad;
    private String unidadEdad; // Nuevo campo
    private String estadoSalud;
    private Long adoptanteId;
    private EstadoAnimal estadoActual;  // Estado actual del animal

    // Constructores

    /**
     * Constructor por defecto.
     */
    public AnimalResponseDto() {
        // Constructor vacío necesario para la deserialización
    }

    /**
     * Constructor parametrizado.
     *
     * @param id            ID del animal.
     * @param nombre        Nombre del animal.
     * @param especie       Especie del animal.
     * @param edad          Edad del animal.
     * @param estadoSalud   Estado de salud del animal.
     * @param adoptanteId   ID del adoptante.
     * @param estadoActual  Estado actual del animal.
     */
    public AnimalResponseDto(Long id, String nombre, String especie, int edad, String unidadEdad, String estadoSalud, Long adoptanteId, EstadoAnimal estadoActual) {
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.edad = edad;
        this.unidadEdad = unidadEdad;
        this.estadoSalud = estadoSalud;
        this.adoptanteId = adoptanteId;
        this.estadoActual = estadoActual;
    }

    // Getters y Setters

    /**
     * Obtiene el ID del animal.
     *
     * @return ID del animal.
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el ID del animal.
     *
     * @param id ID del animal.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del animal.
     *
     * @return Nombre del animal.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del animal.
     *
     * @param nombre Nombre del animal.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la especie del animal.
     *
     * @return Especie del animal.
     */
    public String getEspecie() {
        return especie;
    }

    /**
     * Establece la especie del animal.
     *
     * @param especie Especie del animal.
     */
    public void setEspecie(String especie) {
        this.especie = especie;
    }

    /**
     * Obtiene la edad del animal.
     *
     * @return Edad del animal.
     */
    public int getEdad() {
        return edad;
    }

    /**
     * Establece la edad del animal.
     *
     * @param edad Edad del animal.
     */
    public void setEdad(int edad) {
        this.edad = edad;
    }

    /* Sirve para saber la unidad de la edad (meses, años) */

    public String getUnidadEdad() {
        return unidadEdad;
    }

    public void setUnidadEdad(String unidadEdad) {
        this.unidadEdad = unidadEdad;
    }

    /**
     * Obtiene el estado de salud del animal.
     *
     * @return Estado de salud del animal.
     */
    public String getEstadoSalud() {
        return estadoSalud;
    }

    /**
     * Establece el estado de salud del animal.
     *
     * @param estadoSalud Estado de salud del animal.
     */
    public void setEstadoSalud(String estadoSalud) {
        this.estadoSalud = estadoSalud;
    }

    /**
     * Obtiene el ID del adoptante.
     *
     * @return ID del adoptante.
     */
    public Long getAdoptanteId() {
        return adoptanteId;
    }

    /**
     * Establece el ID del adoptante.
     *
     * @param adoptanteId ID del adoptante.
     */
    public void setAdoptanteId(Long adoptanteId) {
        this.adoptanteId = adoptanteId;
    }

    /**
     * Obtiene el estado actual del animal.
     *
     * @return Estado actual del animal.
     */
    public EstadoAnimal getEstadoActual() {
        return estadoActual;
    }

    /**
     * Establece el estado actual del animal.
     *
     * @param estadoActual Estado actual del animal.
     */
    public void setEstadoActual(EstadoAnimal estadoActual) {
        this.estadoActual = estadoActual;
    }

    // Método toString para facilitar la depuración

    @Override
    public String toString() {
        return "AnimalResponseDto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", especie='" + especie + '\'' +
                ", edad=" + edad +
                ", estadoSalud='" + estadoSalud + '\'' +
                ", adoptanteId=" + adoptanteId +
                ", estadoActual=" + estadoActual +
                '}';
    }
}
