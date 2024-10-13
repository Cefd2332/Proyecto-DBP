package org.e2e.e2e.Vacuna;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class VacunaRepositoryTest {

    @Autowired
    private VacunaRepository vacunaRepository;

    @Test
    void shouldFindVacunaById() {
        Vacuna vacuna = new Vacuna();
        // Configura los atributos de la vacuna
        vacuna = vacunaRepository.save(vacuna);

        Optional<Vacuna> found = vacunaRepository.findById(vacuna.getId());

        assertTrue(found.isPresent());
        assertEquals(vacuna.getId(), found.get().getId());
    }

    // Agrega otros métodos de prueba para diferentes queries o métodos personalizados del repositorio
}

