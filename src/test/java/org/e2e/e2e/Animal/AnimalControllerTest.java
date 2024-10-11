package org.e2e.e2e.Animal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnimalControllerTest {

    @Mock
    private AnimalService animalService;

    @InjectMocks
    private AnimalController animalController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGuardarAnimal() {
        AnimalRequestDto request = new AnimalRequestDto();
        request.setNombre("Gato");

        // Simula el objeto Animal que será devuelto por el servicio
        Animal mockAnimal = new Animal();
        mockAnimal.setNombre("Gato");

        // Simulamos el servicio guardando y devolviendo un Animal
        when(animalService.guardarAnimal(any(AnimalRequestDto.class))).thenReturn(mockAnimal);

        // Simulamos el metodo de conversión del servicio
        AnimalResponseDto responseDto = new AnimalResponseDto();
        responseDto.setNombre("Gato");
        when(animalService.convertirAnimalAResponseDto(any(Animal.class))).thenReturn(responseDto);

        // Llamamos al metodo del controlador
        ResponseEntity<AnimalResponseDto> result = animalController.registrarAnimal(request);

        // Verificamos que el cuerpo de la respuesta no sea nulo y que el nombre sea el esperado
        assertNotNull(result.getBody());
        assertEquals("Gato", result.getBody().getNombre());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

}
