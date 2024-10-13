package org.e2e.e2e.Vacuna;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = VacunaController.class)
@AutoConfigureMockMvc(addFilters = false)  // Desactiva los filtros de seguridad para simplificar los tests
class VacunaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VacunaService vacunaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetVacunasPorAnimal() throws Exception {
        Long animalId = 1L;

        // Simulamos una lista de entidades Vacuna en lugar de VacunaResponseDto
        Vacuna vacuna = new Vacuna();
        vacuna.setId(1L);
        vacuna.setNombre("Vacuna A");
        vacuna.setFechaAplicacion(LocalDate.now());

        List<Vacuna> vacunas = Collections.singletonList(vacuna);

        // Mockeamos el servicio para devolver la lista de entidades Vacuna
        when(vacunaService.obtenerVacunasPorAnimal(animalId)).thenReturn(vacunas);

        // Realizamos la prueba del controlador
        mockMvc.perform(get("/api/vacunas/{animalId}", animalId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nombre").value("Vacuna A"));
    }

    @Test
    void shouldCreateVacuna() throws Exception {
        VacunaRequestDto request = new VacunaRequestDto();
        request.setNombre("Vacuna A");
        request.setFechaAplicacion(LocalDate.now());
        request.setAnimalId(1L);

        Vacuna vacuna = new Vacuna();
        vacuna.setNombre("Vacuna A");

        VacunaResponseDto response = new VacunaResponseDto();
        response.setId(1L);
        response.setNombre("Vacuna A");

        when(vacunaService.guardarVacuna(request)).thenReturn(vacuna);
        when(vacunaService.convertirVacunaAResponseDto(vacuna)).thenReturn(response);

        mockMvc.perform(post("/api/vacunas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Vacuna A"));
    }

    @Test
    void shouldDeleteVacuna() throws Exception {
        Long vacunaId = 1L;

        mockMvc.perform(delete("/api/vacunas/{id}", vacunaId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}





