package org.e2e.e2e.Animal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstadoAnimalTest {

    @Test
    void testEstadoAnimalEnum() {
        assertEquals("SANO", EstadoAnimal.SANO.name());
        assertEquals("ENFERMO", EstadoSalud.ENFERMO.name());
    }
}
