package org.e2e.e2e.Ubicasion;

import org.e2e.e2e.Animal.Animal;
import org.e2e.e2e.Animal.AnimalRepository;
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

    @Autowired
    private AnimalRepository animalRepository; // Suponiendo que tienes un repositorio para Animal

    @Test
    void existsByLatitudAndLongitudAndAnimalId_deberiaRetornarTrueSiExiste() {
        // Crear y guardar el objeto Animal
        Animal animal = new Animal();
        animal.setNombre("Firulais"); // Establecer un nombre u otros atributos si es necesario
        animal = animalRepository.save(animal); // Guardar el animal en la base de datos

        // Ahora crear y guardar la ubicación asociada al animal guardado
        UbicacionAnimal ubicacion = new UbicacionAnimal();
        ubicacion.setLatitud(12.345);
        ubicacion.setLongitud(67.890);
        ubicacion.setAnimal(animal); // Asignar el animal guardado a la ubicación
        ubicacionAnimalRepository.save(ubicacion);

        // Verificar que la ubicación existe
        boolean existe = ubicacionAnimalRepository.existsByLatitudAndLongitudAndAnimalId(12.345, 67.890, animal.getId());
        assertTrue(existe);
    }
}
