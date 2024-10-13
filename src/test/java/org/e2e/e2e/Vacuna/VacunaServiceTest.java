package org.e2e.e2e.Vacuna;
import org.e2e.e2e.Animal.Animal;
import org.e2e.e2e.Animal.AnimalService;
import org.e2e.e2e.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.e2e.e2e.Adopcion.*;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class VacunaServiceTest {

    @Mock
    private AnimalService animalService;

    @Mock
    private VacunaRepository vacunaRepository;

    @InjectMocks
    private VacunaService vacunaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void shouldGuardarVacuna() {
        // Datos de prueba
        VacunaRequestDto requestDto = new VacunaRequestDto();
        requestDto.setAnimalId(1L);
        requestDto.setFechaAplicacion(LocalDate.now());

        Animal animal = new Animal();
        animal.setId(1L);
        when(animalService.obtenerAnimalPorId(requestDto.getAnimalId())).thenReturn(animal);

        Vacuna vacuna = new Vacuna();
        when(vacunaRepository.save(any(Vacuna.class))).thenReturn(vacuna);

        // Llamar al método
        vacunaService.guardarVacuna(requestDto);

        // Verificaciones
        verify(vacunaRepository, times(1)).save(any(Vacuna.class));
    }


    @Test
    void shouldConvertirVacunaAResponseDto() {
        Vacuna vacuna = new Vacuna();
        vacuna.setNombre("Vacuna A");
        vacuna.setId(1L);
        // Debemos asegurarnos de que vacuna tiene un Animal no nulo
        Animal animal = new Animal();
        animal.setId(1L);
        vacuna.setAnimal(animal);

        VacunaResponseDto responseDto = vacunaService.convertirVacunaAResponseDto(vacuna);
        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getId());
        assertEquals("Vacuna A", responseDto.getNombre());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenMissingData() {
        VacunaRequestDto requestDto = new VacunaRequestDto();
        // No se configura ningún campo obligatorio en el DTO para simular el error

        // Simulación del comportamiento de animalService
        Animal animal = new Animal();
        animal.setId(1L);
        when(animalService.obtenerAnimalPorId(1L)).thenReturn(animal);

        // Verificamos que se lanza la excepción BadRequestException
        assertThrows(NotFoundException.class, () -> {
            vacunaService.guardarVacuna(requestDto);
        });

    }
}


