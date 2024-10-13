package org.e2e.e2e.CitaVeterinaria;

import org.e2e.e2e.Animal.Animal;
import org.e2e.e2e.Animal.AnimalService;
import org.e2e.e2e.Notificacion.NotificacionPushService;
import org.e2e.e2e.exceptions.ConflictException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CitaVeterinariaServiceTest {

    @Mock
    private CitaVeterinariaRepository citaVeterinariaRepository;

    @Mock
    private AnimalService animalService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private NotificacionPushService notificacionPushService;

    @InjectMocks
    private CitaVeterinariaService citaVeterinariaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerCitasPorAnimal() {
        // Arrange
        Long animalId = 1L;
        Animal animal = new Animal();
        animal.setCitasVeterinarias(new ArrayList<>());
        when(animalService.obtenerAnimalPorId(animalId)).thenReturn(animal);

        // Act
        List<CitaVeterinaria> citas = citaVeterinariaService.obtenerCitasPorAnimal(animalId);

        // Assert
        assertNotNull(citas);
        assertTrue(citas.isEmpty());
        verify(animalService, times(1)).obtenerAnimalPorId(animalId);
    }


    @Test
    void testGuardarCita_Conflicto() {
        // Arrange
        CitaVeterinariaRequestDto citaDto = new CitaVeterinariaRequestDto();
        citaDto.setFechaCita(LocalDateTime.now().plusDays(1));
        citaDto.setVeterinario("Dr. Smith");
        citaDto.setAnimalId(1L);

        when(animalService.obtenerAnimalPorId(citaDto.getAnimalId())).thenReturn(new Animal());
        when(citaVeterinariaRepository.existsByFechaCitaAndVeterinario(any(), any())).thenReturn(true);

        // Act & Assert
        assertThrows(ConflictException.class, () -> citaVeterinariaService.guardarCita(citaDto));
        verify(citaVeterinariaRepository, never()).save(any(CitaVeterinaria.class));
    }

    // Aquí puedes añadir más tests como:
    // - test para el método de enviarNotificacionesAsync
    // - test para verificar excepciones adicionales o flujos fallidos
}

