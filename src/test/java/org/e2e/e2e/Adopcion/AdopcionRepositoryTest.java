package org.e2e.e2e.Adopcion;

import org.e2e.e2e.Animal.Animal;
import org.e2e.e2e.Animal.AnimalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AdopcionRepositoryTest {

    @Autowired
    private AdopcionRepository adopcionRepository;


    @Autowired
    private AnimalRepository animalRepository;

    private Animal animal;

    @BeforeEach
    void setUp() {
        // Crear un animal de prueba y guardarlo en el repositorio
        animal = new Animal();
        animal.setId(1L);  // Asegúrate de tener un ID si no usas generación automática
        animal.setNombre("Max");
        animalRepository.save(animal);  // Guardar el animal en el repositorio
    }

    @Test
    @Rollback(false) // Desactivar rollback para que los datos se guarden
    void existsByAnimalId_true() {
        // Crear una adopción asociada al animal
        Adopcion adopcion = new Adopcion();
        adopcion.setAnimal(animal); // Asignar el animal a la adopción
        adopcionRepository.save(adopcion); // Guardar la adopción

        // Verificar que el metodo personalizado funciona
        boolean exists = adopcionRepository.existsByAnimalId(animal.getId());
        assertTrue(exists); // Verificar que exista
    }

    @Test
    void existsByAnimalId_false() {
        boolean exists = adopcionRepository.existsByAnimalId(999L);
        assertFalse(exists);
    }
}
