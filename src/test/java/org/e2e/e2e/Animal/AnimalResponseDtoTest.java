package org.e2e.e2e.Animal;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AnimalResponseDtoTest {

    @Test
    void testAnimalResponseDtoAttributes() {
        AnimalResponseDto dto = new AnimalResponseDto();
        dto.setNombre("Perro");

        assertEquals("Perro", dto.getNombre());
    }
}
