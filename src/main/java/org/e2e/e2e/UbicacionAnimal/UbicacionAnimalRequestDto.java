package org.e2e.e2e.UbicacionAnimal;

import jakarta.validation.constraints.NotNull;

public class UbicacionAnimalRequestDto {

    @NotNull(message = "La latitud no puede ser nula")
    private Double latitud;

    @NotNull(message = "La longitud no puede ser nula")
    private Double longitud;

    @NotNull(message = "El ID del animal no puede ser nulo")
    private Long animalId;

    // Constructores
    public UbicacionAnimalRequestDto() {
    }

    public UbicacionAnimalRequestDto(Double latitud, Double longitud, Long animalId) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.animalId = animalId;
    }

    // Getters y Setters
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

    public Long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }

    @Override
    public String toString() {
        return "UbicacionAnimalRequestDto{" +
                "latitud=" + latitud +
                ", longitud=" + longitud +
                ", animalId=" + animalId +
                '}';
    }
}
