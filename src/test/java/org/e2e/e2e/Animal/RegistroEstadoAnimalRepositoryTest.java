package org.e2e.e2e.Animal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RegistroEstadoAnimalRepositoryTest {

    @Autowired
    private RegistroEstadoAnimalRepository registroEstadoAnimalRepository;

    @Test
    void testSaveRegistroEstadoAnimal() {
        RegistroEstadoAnimal registro = new RegistroEstadoAnimal();
        registro.setEstado(EstadoAnimal.SANO);

        RegistroEstadoAnimal savedRegistro = registroEstadoAnimalRepository.save(registro);
        assertNotNull(savedRegistro);
        assertEquals(EstadoAnimal.SANO, savedRegistro.getEstado());
    }

    @Test
    void testFindById() {
        RegistroEstadoAnimal registro = new RegistroEstadoAnimal();
        registro.setEstado(EstadoAnimal.EN_TRATAMIENTO);
        registroEstadoAnimalRepository.save(registro);

        assertTrue(registroEstadoAnimalRepository.findById(registro.getId()).isPresent());
    }
}
