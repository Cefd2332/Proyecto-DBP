package org.e2e.e2e.Animal;

public class AnimalResponseDto {

    private Long id;
    private String nombre;
    private String especie;
    private int edad;
    private String unidadEdad; // Campo para la unidad de edad
    private String estadoSalud;
    private String genero; // Nuevo campo para el género
    private Long adoptanteId;
    private EstadoAnimal estadoActual;  // Estado actual del animal

    // Constructor para función Perfil
    public AnimalResponseDto(Long id, String nombre, String especie, String estadoSalud) {
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.estadoSalud = estadoSalud;
    }

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
     * @param unidadEdad    Unidad de la edad del animal (años/meses).
     * @param estadoSalud   Estado de salud del animal.
     * @param genero        Género del animal.
     * @param adoptanteId   ID del adoptante.
     * @param estadoActual  Estado actual del animal.
     */
    public AnimalResponseDto(Long id, String nombre, String especie, int edad, String unidadEdad, String estadoSalud, String genero, Long adoptanteId, EstadoAnimal estadoActual) {
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.edad = edad;
        this.unidadEdad = unidadEdad;
        this.estadoSalud = estadoSalud;
        this.genero = genero; // Asignar género
        this.adoptanteId = adoptanteId;
        this.estadoActual = estadoActual;
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

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Long getAdoptanteId() {
        return adoptanteId;
    }

    public void setAdoptanteId(Long adoptanteId) {
        this.adoptanteId = adoptanteId;
    }

    public EstadoAnimal getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(EstadoAnimal estadoActual) {
        this.estadoActual = estadoActual;
    }

    @Override
    public String toString() {
        return "AnimalResponseDto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", especie='" + especie + '\'' +
                ", edad=" + edad +
                ", unidadEdad='" + unidadEdad + '\'' +
                ", estadoSalud='" + estadoSalud + '\'' +
                ", genero='" + genero + '\'' +
                ", adoptanteId=" + adoptanteId +
                ", estadoActual=" + estadoActual +
                '}';
    }
}
