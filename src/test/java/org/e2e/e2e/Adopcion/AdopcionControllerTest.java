package org.e2e.e2e.Adopcion;
import org.e2e.e2e.Animal.Animal;
import org.e2e.e2e.Usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AdopcionControllerTest {

    @Mock
    private AdopcionService adopcionService;

    @InjectMocks
    private AdopcionController adopcionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void obtenerTodasLasAdopciones_exito() {
        // Simulación de la entidad Adopcion
        Adopcion adopcion = new Adopcion();
        adopcion.setId(1L);

        Animal animal = new Animal();
        animal.setId(1L);
        adopcion.setAnimal(animal);

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        adopcion.setAdoptante(usuario);

        // Simular el retorno de una lista de adopciones desde el servicio
        List<Adopcion> listaAdopciones = Collections.singletonList(adopcion);
        doReturn(listaAdopciones).when(adopcionService).obtenerTodasLasAdopciones();

        // Simulación de la conversión de Adopcion a AdopcionResponseDto
        AdopcionResponseDto responseDto = new AdopcionResponseDto();
        responseDto.setId(1L);
        responseDto.setAnimalId(1L);
        responseDto.setAdoptanteId(1L);
        doReturn(responseDto).when(adopcionService).convertirAdopcionAResponseDto(adopcion);

        // Llamar al método del controlador
        ResponseEntity<List<AdopcionResponseDto>> response = adopcionController.obtenerAdopciones();

        // Verificaciones
        assertEquals(HttpStatus.OK, response.getStatusCode()); // Verificamos que el estado es OK
        assertNotNull(response.getBody(), "El cuerpo de la respuesta no debe ser null"); // Verificamos que la respuesta no sea null
        assertFalse(response.getBody().isEmpty(), "La lista no debe estar vacía"); // Verificamos que la lista no esté vacía
        assertNotNull(response.getBody().get(0), "El primer elemento no debe ser null"); // Verificamos que el primer elemento no sea null
        assertEquals(1L, response.getBody().get(0).getId(), "El ID del primer elemento debe ser 1L"); // Verificamos que el ID del primer elemento es correcto
    }


    @Test
    void registrarAdopcion_exito() {
        // Datos de prueba
        AdopcionRequestDto requestDto = new AdopcionRequestDto();
        requestDto.setAnimalId(1L);
        requestDto.setAdoptanteId(1L);
        requestDto.setFechaAdopcion(java.time.LocalDate.now());

        // Simulación de la respuesta esperada del servicio
        Adopcion adopcion = new Adopcion();
        adopcion.setId(1L);

        Animal animal = new Animal();  // Aseguramos que Animal no sea null
        animal.setId(1L);
        adopcion.setAnimal(animal);

        Usuario usuario = new Usuario();  // Aseguramos que Usuario no sea null
        usuario.setId(1L);
        adopcion.setAdoptante(usuario);

        AdopcionResponseDto responseDto = new AdopcionResponseDto();
        responseDto.setId(1L);
        responseDto.setAnimalId(1L);
        responseDto.setAdoptanteId(1L);
        responseDto.setFechaAdopcion(java.time.LocalDate.now());

        // Simula que registrarAdopcion devuelva un objeto de tipo Adopcion
        doReturn(adopcion).when(adopcionService).registrarAdopcion(any(AdopcionRequestDto.class));

        // Simula la conversión de Adopcion a AdopcionResponseDto
        doReturn(responseDto).when(adopcionService).convertirAdopcionAResponseDto(adopcion);

        // Llamar al método del controlador
        ResponseEntity<AdopcionResponseDto> response = adopcionController.registrarAdopcion(requestDto);

        // Verificaciones
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }


    @Test
    void obtenerAdopcionPorId_exito() {
        // Simulación de la respuesta esperada del servicio
        Adopcion adopcion = new Adopcion();
        adopcion.setId(1L);

        Animal animal = new Animal();  // Aseguramos que Animal no sea null
        animal.setId(1L);
        adopcion.setAnimal(animal);

        Usuario usuario = new Usuario();  // Aseguramos que Usuario no sea null
        usuario.setId(1L);
        adopcion.setAdoptante(usuario);

        // Creamos un DTO con la misma información que la entidad
        AdopcionResponseDto responseDto = new AdopcionResponseDto();
        responseDto.setId(1L);
        responseDto.setAnimalId(1L);
        responseDto.setAdoptanteId(1L);
        responseDto.setFechaAdopcion(java.time.LocalDate.now());

        // Simula que obtenerAdopcionPorId devuelva un objeto de tipo Adopcion
        doReturn(adopcion).when(adopcionService).obtenerAdopcionPorId(1L);

        // Simula la conversión de Adopcion a AdopcionResponseDto
        doReturn(responseDto).when(adopcionService).convertirAdopcionAResponseDto(adopcion);

        // Llamar al método del controlador
        ResponseEntity<AdopcionResponseDto> response = adopcionController.obtenerAdopcionPorId(1L);

        // Verificaciones
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void eliminarAdopcion_exito() {
        // Simular la eliminación exitosa
        doNothing().when(adopcionService).eliminarAdopcion(1L);

        // Llamar al método del controlador
        ResponseEntity<Void> response = adopcionController.eliminarAdopcion(1L);

        // Verificar el código de respuesta
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}

