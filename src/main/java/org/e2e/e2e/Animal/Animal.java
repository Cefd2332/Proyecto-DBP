package org.e2e.e2e.Animal;

import jakarta.persistence.*;
import org.e2e.e2e.Adoptante.Adoptante;
import org.e2e.e2e.CitaVeterinaria.CitaVeterinaria;
import org.e2e.e2e.RegistroSalud.RegistroSalud;
import org.e2e.e2e.UbicacionAnimal.UbicacionAnimal;
import org.e2e.e2e.Vacuna.Vacuna;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "animales") // Especifica el nombre de la tabla en la base de datos
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String especie;

    @Column(nullable = false)
    private int edad;

    @Column(nullable = false, length = 100)
    private String estadoSalud;

    @Column(nullable = true)
    private LocalDate fechaAdopcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoAnimal estadoActual;  // Estado actual del animal

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adoptante_id", nullable = false)
    private Adoptante adoptante; // Cambiado de Usuario a Adoptante

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistroEstadoAnimal> registroEstadoAnimal = new ArrayList<>();

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistroSalud> historialMedico = new ArrayList<>();

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CitaVeterinaria> citasVeterinarias = new ArrayList<>();

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UbicacionAnimal> ubicaciones = new ArrayList<>();

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vacuna> vacunas = new ArrayList<>();

    // Constructores

    public Animal() {
        // Constructor por defecto
    }

    public Animal(String nombre, String especie, int edad, String estadoSalud, LocalDate fechaAdopcion, EstadoAnimal estadoActual, Adoptante adoptante) {
        this.nombre = nombre;
        this.especie = especie;
        this.edad = edad;
        this.estadoSalud = estadoSalud;
        this.fechaAdopcion = fechaAdopcion;
        this.estadoActual = estadoActual;
        this.adoptante = adoptante;
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    // No se proporciona setId ya que generalmente el ID es generado automáticamente

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

    public Adoptante getAdoptante() {
        return adoptante;
    }

    public void setAdoptante(Adoptante adoptante) {
        this.adoptante = adoptante;
    }

    public List<RegistroEstadoAnimal> getRegistroEstadoAnimal() {
        return registroEstadoAnimal;
    }

    public void setRegistroEstadoAnimal(List<RegistroEstadoAnimal> registroEstadoAnimal) {
        this.registroEstadoAnimal = registroEstadoAnimal;
    }

    public List<RegistroSalud> getHistorialMedico() {
        return historialMedico;
    }

    public void setHistorialMedico(List<RegistroSalud> historialMedico) {
        this.historialMedico = historialMedico;
    }

    public List<CitaVeterinaria> getCitasVeterinarias() {
        return citasVeterinarias;
    }

    public void setCitasVeterinarias(List<CitaVeterinaria> citasVeterinarias) {
        this.citasVeterinarias = citasVeterinarias;
    }

    public List<UbicacionAnimal> getUbicaciones() {
        return ubicaciones;
    }

    public void setUbicaciones(List<UbicacionAnimal> ubicaciones) {
        this.ubicaciones = ubicaciones;
    }

    public List<Vacuna> getVacunas() {
        return vacunas;
    }

    public void setVacunas(List<Vacuna> vacunas) {
        this.vacunas = vacunas;
    }

    // Métodos auxiliares para manejar relaciones bidireccionales

    public void addRegistroSalud(RegistroSalud registroSalud) {
        historialMedico.add(registroSalud);
        registroSalud.setAnimal(this);
    }

    public void removeRegistroSalud(RegistroSalud registroSalud) {
        historialMedico.remove(registroSalud);
        registroSalud.setAnimal(null);
    }

    public void addRegistroEstadoAnimal(RegistroEstadoAnimal registroEstadoAnimal) {
        this.registroEstadoAnimal.add(registroEstadoAnimal);
        registroEstadoAnimal.setAnimal(this);
    }

    public void removeRegistroEstadoAnimal(RegistroEstadoAnimal registroEstadoAnimal) {
        this.registroEstadoAnimal.remove(registroEstadoAnimal);
        registroEstadoAnimal.setAnimal(null);
    }

    public void addCitaVeterinaria(CitaVeterinaria citaVeterinaria) {
        this.citasVeterinarias.add(citaVeterinaria);
        citaVeterinaria.setAnimal(this);
    }

    public void removeCitaVeterinaria(CitaVeterinaria citaVeterinaria) {
        this.citasVeterinarias.remove(citaVeterinaria);
        citaVeterinaria.setAnimal(null);
    }

    public void addUbicacion(UbicacionAnimal ubicacionAnimal) {
        this.ubicaciones.add(ubicacionAnimal);
        ubicacionAnimal.setAnimal(this);
    }

    public void removeUbicacion(UbicacionAnimal ubicacionAnimal) {
        this.ubicaciones.remove(ubicacionAnimal);
        ubicacionAnimal.setAnimal(null);
    }

    public void addVacuna(Vacuna vacuna) {
        this.vacunas.add(vacuna);
        vacuna.setAnimal(this);
    }

    public void removeVacuna(Vacuna vacuna) {
        this.vacunas.remove(vacuna);
        vacuna.setAnimal(null);
    }
}
