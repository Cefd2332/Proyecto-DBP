package org.e2e.e2e.Animal;

import org.e2e.e2e.Animal.Animal;
import org.e2e.e2e.Animal.EstadoAnimal;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {

    @Test
    void testAnimalAttributes() {
        Animal animal = new Animal();
        animal.setId(1L);
        animal.setNombre("Perro");
        animal.setEdad(5);
        animal.setEstadoActual(EstadoAnimal.SANO);

        assertEquals(1L, animal.getId());
        assertEquals("Perro", animal.getNombre());
        assertEquals(5, animal.getEdad());
        assertEquals(EstadoAnimal.SANO, animal.getEstadoActual());
    }

    @Test
    void testAnimalToString() {
        Animal animal = new Animal();
        animal.setNombre("Gato");
        String expected = "Animal{name='Gato'}";
        assertTrue(animal.toString().contains("Gato"));
    }
}
