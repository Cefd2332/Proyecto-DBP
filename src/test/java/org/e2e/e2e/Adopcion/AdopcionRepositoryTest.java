package org.e2e.e2e;
import org.e2e.e2e.Adopcion.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AdopcionRepositoryTest {

    @Autowired
    private AdopcionRepository adopcionRepository;

    @Test
    void existsByAnimalId_true() {
        // Configurar datos de prueba
        Adopcion adopcion = new Adopcion();
        adopcion.Id(1L);
        adopcionRepository.save(adopcion);

        // Verificar que el método personalizado funciona
        boolean exists = adopcionRepository.existsByAnimalId(1L);
        assertTrue(exists);
    }

    @Test
    void existsByAnimalId_false() {
        boolean exists = adopcionRepository.existsByAnimalId(999L);
        assertFalse(exists);
    }
}
