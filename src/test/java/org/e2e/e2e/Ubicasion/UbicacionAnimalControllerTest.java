package org.e2e.e2e.Ubicasion;

import org.e2e.e2e.UbicacionAnimal.UbicacionAnimalRequestDto;
import org.e2e.e2e.UbicacionAnimal.UbicacionAnimalResponseDto;
import org.e2e.e2e.UbicacionAnimal.UbicacionAnimalService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UbicacionAnimalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UbicacionAnimalService ubicacionAnimalService;

    @Test
    void obtenerUbicaciones_deberiaRetornar200() throws Exception {
        Long animalId = 1L;
        List<UbicacionAnimalResponseDto> ubicaciones = new ArrayList<>();
        when(ubicacionAnimalService.obtenerUbicaciones(animalId)).thenReturn(ubicaciones);

        mockMvc.perform(get("/api/ubicaciones/{animalId}", animalId))
                .andExpect(status().isOk());
    }

    @Test
    void registrarUbicacion_deberiaRetornar200() throws Exception {
        UbicacionAnimalRequestDto requestDto = new UbicacionAnimalRequestDto();
        UbicacionAnimalResponseDto responseDto = new UbicacionAnimalResponseDto();
        when(ubicacionAnimalService.guardarUbicacion(any())).thenReturn(responseDto);

        mockMvc.perform(post("/api/ubicaciones")
                        .contentType(MediaType.APPLICATION_JSON) // Importación correcta
                        .content("{ \"latitud\": 12.345, \"longitud\": 67.890, \"animalId\": 1 }"))
                .andExpect(status().isOk());
    }
}
