package org.e2e.e2e.Adopcion;
import org.e2e.e2e.Animal.Animal;
import org.e2e.e2e.Animal.AnimalRepository;
import org.e2e.e2e.Animal.AnimalService;
import org.e2e.e2e.Email.EmailEvent;
import org.e2e.e2e.Notificacion.NotificacionPushService;
import org.e2e.e2e.Usuario.Usuario;
import org.e2e.e2e.Usuario.UsuarioService;
import org.e2e.e2e.exceptions.ConflictException;
import org.e2e.e2e.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdopcionServiceTest {

    @Mock
    private AdopcionRepository adopcionRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private AnimalService animalService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private NotificacionPushService notificacionPushService;

    @InjectMocks
    private AdopcionService adopcionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registrarAdopcion_exito() {
        // Datos de prueba
        AdopcionRequestDto requestDto = new AdopcionRequestDto();
        requestDto.setFechaAdopcion(LocalDate.now());
        requestDto.setAdoptanteId(1L);
        requestDto.setAnimalId(1L);

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Carlos");
        usuario.setEmail("carlos@example.com");

        Animal animal = new Animal();
        animal.setId(1L);
        animal.setNombre("Max");

        // Simular respuestas de los servicios dependientes
        when(usuarioService.obtenerUsuarioPorId(1L)).thenReturn(usuario);
        when(animalService.obtenerAnimalPorId(1L)).thenReturn(animal);
        when(adopcionRepository.existsByAnimalId(1L)).thenReturn(false);

        // Crear instancia de Adopcion y simular el guardado en el repositorio
        Adopcion adopcion = new Adopcion();
        adopcion.setId(1L);
        adopcion.setFechaAdopcion(LocalDate.now());
        adopcion.setAdoptante(usuario);
        adopcion.setAnimal(animal);

        when(adopcionRepository.save(any(Adopcion.class))).thenReturn(adopcion);

        // Ejecución del método a probar
        Adopcion adopcionGuardada = adopcionService.registrarAdopcion(requestDto);

        // Verificaciones
        assertNotNull(adopcionGuardada);
        assertEquals(1L, adopcionGuardada.getAdoptante().getId());
        assertEquals(1L, adopcionGuardada.getAnimal().getId());
        verify(adopcionRepository, times(1)).save(any(Adopcion.class));

        // Verificar que se envía la notificación de correo electrónico
        verify(eventPublisher, times(1)).publishEvent(any(EmailEvent.class));

        // Verificar que se envía una notificación push (si el token está disponible)
        verify(notificacionPushService, never()).enviarNotificacion(anyString(), anyString(), anyString());
    }

    @Test
    void registrarAdopcion_animalYaAdoptado() {
        // Datos de prueba
        AdopcionRequestDto requestDto = new AdopcionRequestDto();
        requestDto.setFechaAdopcion(LocalDate.now());
        requestDto.setAdoptanteId(1L);
        requestDto.setAnimalId(1L);

        // Crear un animal de prueba
        Animal animal = new Animal();
        animal.setId(1L);
        animal.setNombre("Max");

        // Simular que el animal existe y es devuelto por el repositorio
        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));

        // Simular que ya ha sido adoptado
        when(adopcionRepository.existsByAnimalId(1L)).thenReturn(true);

        // Verificar que se lanza la excepción ConflictException
        assertThrows(ConflictException.class, () -> adopcionService.registrarAdopcion(requestDto));
    }

    @Test
    void obtenerAdopcionPorId_adopcionNoEncontrada() {
        // Simular que no se encuentra la adopción por ID
        when(adopcionRepository.findById(1L)).thenReturn(Optional.empty());

        // Verificar que se lanza la excepción NotFoundException
        assertThrows(NotFoundException.class, () -> adopcionService.obtenerAdopcionPorId(1L));
    }

    @Test
    void eliminarAdopcion_exito() {
        // Simular que la adopción existe
        when(adopcionRepository.existsById(1L)).thenReturn(true);

        // Ejecutar el método
        adopcionService.eliminarAdopcion(1L);

        // Verificar que el método deleteById fue llamado
        verify(adopcionRepository, times(1)).deleteById(1L);
    }

    @Test
    void eliminarAdopcion_adopcionNoEncontrada() {
        // Simular que la adopción no existe
        when(adopcionRepository.existsById(1L)).thenReturn(false);

        // Verificar que se lanza la excepción NotFoundException
        assertThrows(NotFoundException.class, () -> adopcionService.eliminarAdopcion(1L));
    }
}
