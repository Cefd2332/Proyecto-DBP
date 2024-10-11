package org.e2e.e2e.Adopcion;
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
        // Simulación de la respuesta esperada del servicio
        AdopcionResponseDto responseDto = new AdopcionResponseDto();
        responseDto.setId(1L);
        responseDto.setAnimalId(1L);
        responseDto.setAdoptanteId(1L);

        // Simular el retorno de una lista de adopciones desde el servicio
        doReturn(Collections.singletonList(responseDto)).when(adopcionService).obtenerTodasLasAdopciones();

        // Llamar al método del controlador
        ResponseEntity<List<AdopcionResponseDto>> response = adopcionController.obtenerAdopciones();

        // Verificaciones
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(1L, response.getBody().get(0).getId());
    }

    @Test
    void registrarAdopcion_exito() {
        // Datos de prueba
        AdopcionRequestDto requestDto = new AdopcionRequestDto();
        requestDto.setAnimalId(1L);
        requestDto.setAdoptanteId(1L);
        requestDto.setFechaAdopcion(java.time.LocalDate.now());

        // Simulación de la respuesta esperada del servicio
        AdopcionResponseDto responseDto = new AdopcionResponseDto();
        responseDto.setId(1L);

        // Simular el comportamiento del servicio
        doReturn(responseDto).when(adopcionService).registrarAdopcion(any(AdopcionRequestDto.class));

        // Llamar al método del controlador
        ResponseEntity<AdopcionResponseDto> response = adopcionController.registrarAdopcion(requestDto);

        // Verificaciones
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
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

    @Test
    void obtenerAdopcionPorId_exito() {
        // Simulación de la respuesta esperada del servicio
        AdopcionResponseDto responseDto = new AdopcionResponseDto();
        responseDto.setId(1L);
        responseDto.setAnimalId(1L);
        responseDto.setAdoptanteId(1L);

        // Simular el retorno desde el servicio
        doReturn(responseDto).when(adopcionService).obtenerAdopcionPorId(1L);

        // Llamar al método del controlador
        ResponseEntity<AdopcionResponseDto> response = adopcionController.obtenerAdopcionPorId(1L);

        // Verificaciones
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }
}

