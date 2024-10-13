package org.e2e.e2e.Ubicasion;

import org.e2e.e2e.Animal.Animal;
import org.e2e.e2e.UbicacionAnimal.UbicacionAnimal;
import org.e2e.e2e.UbicacionAnimal.UbicacionAnimalRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.Assert.assertTrue;

@DataJpaTest
class UbicacionAnimalRepositoryTest {

    @Autowired
    private UbicacionAnimalRepository ubicacionAnimalRepository;

    @Test
    void existsByLatitudAndLongitudAndAnimalId_deberiaRetornarTrueSiExiste() {
        // Dado que ya hay una ubicación guardada en la base de datos
        UbicacionAnimal ubicacion = new UbicacionAnimal();
        ubicacion.setLatitud(12.345);
        ubicacion.setLongitud(67.890);
        ubicacion.setAnimal(new Animal()); // Añadir objeto de animal
        ubicacionAnimalRepository.save(ubicacion);

        boolean existe = ubicacionAnimalRepository.existsByLatitudAndLongitudAndAnimalId(12.345, 67.890, 1L);
        assertTrue(existe);
    }
}
