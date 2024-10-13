package org.e2e.e2e.CitaVeterinaria;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@DataJpaTest
public class CitaVeterinariaRepositoryTest {

    @Autowired
    private CitaVeterinariaRepository citaVeterinariaRepository;

    @Test
    public void testGuardarYBuscarCitaVeterinaria() {
        // Crear una instancia de CitaVeterinaria con valores válidos
        CitaVeterinaria cita = new CitaVeterinaria();
        cita.setFechaCita(LocalDateTime.now().plusDays(1)); // Fecha en el futuro
        cita.setEstado(EstadoCita.PENDIENTE);
        cita.setVeterinario("Dr. Juan Pérez"); // Veterinario no vacío

        // Guardar la cita en la base de datos
        CitaVeterinaria citaGuardada = citaVeterinariaRepository.save(cita);

        // Verificar que la cita fue guardada correctamente
        Assertions.assertNotNull(citaGuardada.getId());

        // Buscar la cita por ID
        Optional<CitaVeterinaria> citaEncontrada = citaVeterinariaRepository.findById(citaGuardada.getId());

        // Verificar que la cita fue encontrada y sus campos son correctos
        Assertions.assertTrue(citaEncontrada.isPresent());
        Assertions.assertEquals(EstadoCita.PENDIENTE, citaEncontrada.get().getEstado());
        Assertions.assertEquals("Dr. Juan Pérez", citaEncontrada.get().getVeterinario());
    }

    @Test
    public void testBuscarCitaVeterinariaPorId() {
        // Crear una instancia de CitaVeterinaria con valores válidos
        CitaVeterinaria cita = new CitaVeterinaria();
        cita.setFechaCita(LocalDateTime.now().plusDays(1)); // Fecha en el futuro
        cita.setEstado(EstadoCita.PENDIENTE);
        cita.setVeterinario("Dr. Juan Pérez"); // Veterinario no vacío

        // Guardar la cita en la base de datos
        CitaVeterinaria citaGuardada = citaVeterinariaRepository.save(cita);

        // Buscar la cita por ID
        Optional<CitaVeterinaria> citaEncontrada = citaVeterinariaRepository.findById(citaGuardada.getId());

        // Verificar que la cita fue encontrada
        assertThat(citaEncontrada).isPresent();
        assertThat(citaEncontrada.get().getId()).isEqualTo(citaGuardada.getId());
    }
}

