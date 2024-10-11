package org.e2e.e2e.Animal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AnimalRepositoryTest {

    @Autowired
    private AnimalRepository animalRepository;

    @Test
    void testFindById() {
        Animal animal = new Animal();
        animal.setNombre("Perro");
        animalRepository.save(animal);

        assertTrue(animalRepository.findById(animal.getId()).isPresent());
    }

    @Test
    void testSaveAnimal() {
        Animal animal = new Animal();
        animal.setNombre("Gato");

        Animal savedAnimal = animalRepository.save(animal);
        assertNotNull(savedAnimal);
        assertEquals("Gato", savedAnimal.getNombre());
    }
}
