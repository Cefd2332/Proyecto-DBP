package org.e2e.e2e.Ubicasion;

import org.e2e.e2e.UbicacionAnimal.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UbicacionAnimalServiceTest {

    @Mock
    private UbicacionAnimalRepository ubicacionAnimalRepository;

    @InjectMocks
    private UbicacionAnimalService ubicacionAnimalService;

    @Test
    void guardarUbicacion_deberiaGuardarCorrectamente() {
        // Mocking request DTO
        UbicacionAnimalRequestDto requestDto = new UbicacionAnimalRequestDto();
        requestDto.setLatitud(12.345);
        requestDto.setLongitud(67.890);
        requestDto.setAnimalId(1L);

        // Mocking UbicacionAnimal entity
        UbicacionAnimal ubicacionAnimal = new UbicacionAnimal();
        ubicacionAnimal.setLatitud(12.345);
        ubicacionAnimal.setLongitud(67.890);

        // Mocking repository behavior
        when(ubicacionAnimalRepository.save(any(UbicacionAnimal.class))).thenReturn(ubicacionAnimal);

        // Act
        UbicacionAnimalResponseDto responseDto = ubicacionAnimalService.guardarUbicacion(requestDto);

        // Assert
        assertNotNull(responseDto);
        assertEquals(12.345, responseDto.getLatitud(), 0.001);  // Precisión delta para evitar ambigüedad
        assertEquals(67.890, responseDto.getLongitud(), 0.001);  // Precisión delta para evitar ambigüedad
    }
}


