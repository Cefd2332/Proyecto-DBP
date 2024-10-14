package org.e2e.e2e.Ubicasion;

import org.e2e.e2e.Animal.Animal;
import org.e2e.e2e.Notificacion.NotificacionPushService;
import org.e2e.e2e.UbicacionAnimal.*;
import org.e2e.e2e.Animal.AnimalService;
import org.e2e.e2e.Usuario.Usuario;
import org.e2e.e2e.Usuario.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.e2e.e2e.Email.EmailEvent;
import org.e2e.e2e.Notificacion.NotificacionPushService;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Usamos MockitoExtension para evitar cargar el contexto completo
class UbicacionAnimalServiceTest {


    @Mock
    private AnimalService animalService;

    @Mock
    private UbicacionAnimalRepository ubicacionAnimalRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private NotificacionPushService notificacionPushService;

    @InjectMocks
    private UbicacionAnimalService ubicacionAnimalService;

    @BeforeEach
    void setUp() {
        // Mocking necessary dependencies
        animalService = mock(AnimalService.class);
        ubicacionAnimalRepository = mock(UbicacionAnimalRepository.class);
        eventPublisher = mock(ApplicationEventPublisher.class); // Mocking ApplicationEventPublisher
        notificacionPushService = mock(NotificacionPushService.class); // Mocking NotificacionPushService

        // Injecting mocks into the service
        ubicacionAnimalService = new UbicacionAnimalService(ubicacionAnimalRepository, animalService, eventPublisher, notificacionPushService);

        // Mocking an animal object
        Animal mockAnimal = new Animal();
        mockAnimal.setId(1L);
        mockAnimal.setNombre("Firulais");

        // Mocking adoptante for the animal
        Usuario adoptante = new Usuario();
        adoptante.setId(1L);
        adoptante.setNombre("John Doe");
        adoptante.setEmail("johndoe@example.com");
        adoptante.setToken("mockToken"); // Añadir un token válido para simular la notificación push

        mockAnimal.setAdoptante(adoptante);

        // Simulating the behavior of obtenerAnimalPorId to return the mocked animal
        when(animalService.obtenerAnimalPorId(1L)).thenReturn(mockAnimal);

        // Mocking UbicacionAnimalRequestDto
        UbicacionAnimalRequestDto ubicacionDto = new UbicacionAnimalRequestDto();
        ubicacionDto.setLatitud(12.345);
        ubicacionDto.setLongitud(67.890);
        ubicacionDto.setAnimalId(1L);  // Ensure that this ID is the same as the mockAnimal ID

        // Mocking the save behavior in the repository
        when(ubicacionAnimalRepository.save(any(UbicacionAnimal.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void guardarUbicacion_deberiaGuardarCorrectamente() {
        // Crear e inicializar el requestDto
        UbicacionAnimalRequestDto requestDto = new UbicacionAnimalRequestDto();
        requestDto.setLatitud(12.345);
        requestDto.setLongitud(67.890);
        requestDto.setAnimalId(1L);  // Debes asegurarte de que este ID sea el correcto para tu prueba.

        // Actuar
        UbicacionAnimalResponseDto responseDto = ubicacionAnimalService.guardarUbicacion(requestDto);

        // Afirmaciones para la prueba
        assertNotNull(responseDto);
        assertEquals(12.345, responseDto.getLatitud(), 0.001);  // Precisión delta
        assertEquals(67.890, responseDto.getLongitud(), 0.001);  // Precisión delta
    }
}

