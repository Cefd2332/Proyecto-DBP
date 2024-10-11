package org.e2e.e2e.Animal;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AnimalRequestDtoTest {

    @Test
    void testAnimalRequestDtoAttributes() {
        AnimalRequestDto dto = new AnimalRequestDto();
        dto.setNombre("Perro");

        assertEquals("Perro", dto.getNombre());
    }
}
