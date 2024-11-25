package org.e2e.e2e.Animal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum GeneroAnimal {
    MACHO("MACHO"),
    HEMBRA("HEMBRA");

    private final String valor;

    GeneroAnimal(String valor) {
        this.valor = valor;
    }

    @JsonValue
    public String getValor() {
        return valor;
    }

    @JsonCreator
    public static GeneroAnimal forValue(String value) {
        for (GeneroAnimal genero : GeneroAnimal.values()) {
            if (genero.valor.equalsIgnoreCase(value)) {
                return genero;
            }
        }
        throw new IllegalArgumentException("Género inválido: " + value);
    }
}
