package org.e2e.e2e.UbicacionAnimal;

import java.time.LocalDateTime;

public class UbicacionAnimalResponseDto {

    private Long id;
    private Double latitud;
    private Double longitud;
    private LocalDateTime fechaHora;
    private Long animalId;

    // Constructores
    public UbicacionAnimalResponseDto() {
    }

    public UbicacionAnimalResponseDto(Long id, Double latitud, Double longitud, LocalDateTime fechaHora, Long animalId) {
        this.id = id;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaHora = fechaHora;
        this.animalId = animalId;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }

    @Override
    public String toString() {
        return "UbicacionAnimalResponseDto{" +
                "id=" + id +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", fechaHora=" + fechaHora +
                ", animalId=" + animalId +
                '}';
    }
}
