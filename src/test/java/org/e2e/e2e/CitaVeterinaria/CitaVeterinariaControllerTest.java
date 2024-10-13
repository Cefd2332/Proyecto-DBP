package org.e2e.e2e.CitaVeterinaria;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;  // Cambiamos a doReturn
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.e2e.e2e.Animal.AnimalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

@WebMvcTest(CitaVeterinariaController.class)
public class CitaVeterinariaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CitaVeterinariaService citaVeterinariaService;

    @MockBean
    private AnimalService animalService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCrearCitaVeterinaria() throws Exception {
        // Preparar request y response DTOs
        CitaVeterinariaRequestDto requestDto = new CitaVeterinariaRequestDto();
        requestDto.setFechaCita(LocalDateTime.now().plusDays(1));
        requestDto.setVeterinario("Dr. Smith");
        requestDto.setAnimalId(1L);
        requestDto.setEstado(EstadoCita.PENDIENTE);

        CitaVeterinariaResponseDto responseDto = new CitaVeterinariaResponseDto();
        responseDto.setId(1L);
        responseDto.setVeterinario("Dr. Smith");

        // Usamos doReturn() para evitar problemas con la inferencia de tipos
        doReturn(responseDto).when(citaVeterinariaService).guardarCita(any(CitaVeterinariaRequestDto.class));

        // Ejecutar la solicitud POST
        mockMvc.perform(post("/citas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.veterinario").value("Dr. Smith"));
    }

    @Test
    public void testCancelarCitaVeterinaria() throws Exception {
        // Simular el método de eliminación
        doNothing().when(citaVeterinariaService).eliminarCita(anyLong());

        // Realizar la solicitud DELETE
        mockMvc.perform(delete("/citas/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testObtenerCitaVeterinariaPorId() throws Exception {
        // Preparar el DTO de respuesta
        CitaVeterinariaResponseDto responseDto = new CitaVeterinariaResponseDto();
        responseDto.setId(1L);
        responseDto.setVeterinario("Dr. Smith");

        // Simular la lógica del servicio
        doReturn(responseDto).when(citaVeterinariaService).obtenerCitasPorAnimal(anyLong());

        // Realizar la solicitud GET
        mockMvc.perform(get("/citas/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.veterinario").value("Dr. Smith"));
    }

    @Test
    public void testActualizarCitaVeterinaria() throws Exception {
        // Preparar los DTOs de solicitud y respuesta
        CitaVeterinariaRequestDto requestDto = new CitaVeterinariaRequestDto();
        requestDto.setFechaCita(LocalDateTime.now().plusDays(1));
        requestDto.setVeterinario("Dr. Smith");
        requestDto.setAnimalId(1L);

        CitaVeterinariaResponseDto responseDto = new CitaVeterinariaResponseDto();
        responseDto.setId(1L);
        responseDto.setVeterinario("Dr. Smith");

        // Simular la lógica del servicio
        doReturn(responseDto).when(citaVeterinariaService).actualizarEstadoCita(anyLong(), any(CitaVeterinariaRequestDto.class).getEstado());

        // Realizar la solicitud PUT
        mockMvc.perform(put("/citas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.veterinario").value("Dr. Smith"));
    }
}









