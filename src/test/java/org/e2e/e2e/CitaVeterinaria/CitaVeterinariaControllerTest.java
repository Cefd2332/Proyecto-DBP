package org.e2e.e2e.CitaVeterinaria;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.e2e.e2e.Animal.Animal;
import org.e2e.e2e.Animal.AnimalService;
import org.e2e.e2e.security.jwt.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.e2e.e2e.security.CustomUserDetailsService;
import org.e2e.e2e.security.jwt.JwtAuthenticationFilter;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = CitaVeterinariaController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
public class CitaVeterinariaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CitaVeterinariaService citaVeterinariaService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private AnimalService animalService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void testPermitirFechaInvalidaEnRequest() throws Exception {
        // DTO con fecha en el pasado, pero permitimos su creación
        CitaVeterinariaRequestDto requestDto = new CitaVeterinariaRequestDto();
        requestDto.setFechaCita(LocalDateTime.now().minusDays(1)); // Fecha en el pasado
        requestDto.setVeterinario("Dr. Smith");
        requestDto.setAnimalId(1L);
        requestDto.setEstado(EstadoCita.PENDIENTE);

        // Ejecutar la prueba y verificar el estado HTTP 200 (OK)
        mockMvc.perform(post("/api/citas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());  // Espera un 200 OK, permitiendo la fecha inválida
    }

    @Test
    public void testEliminarCitaVeterinaria() throws Exception {
        // Simular comportamiento del servicio
        doNothing().when(citaVeterinariaService).eliminarCita(1L);

        // Ejecutar la solicitud DELETE
        mockMvc.perform(delete("/api/citas/1"))
                .andExpect(status().isOk());  // Verificamos que devuelve 200

    }

    @Test
    public void testObtenerCitasPorAnimal() throws Exception {
        // Crear y preparar la entidad Animal y la CitaVeterinaria
        Animal animal = new Animal();
        animal.setId(1L);
        animal.setNombre("Fido");

        CitaVeterinaria citaVeterinaria = new CitaVeterinaria();
        citaVeterinaria.setId(1L);
        citaVeterinaria.setVeterinario("Dr. Smith");
        citaVeterinaria.setFechaCita(LocalDateTime.now().plusDays(1));
        citaVeterinaria.setAnimal(animal);  // Relación con Animal
        citaVeterinaria.setEstado(EstadoCita.PENDIENTE);

        // Simular el retorno de la lista de citas del animal
        List<CitaVeterinaria> citasList = Collections.singletonList(citaVeterinaria);
        when(citaVeterinariaService.obtenerCitasPorAnimal(1L)).thenReturn(citasList);

        // Ejecutar la petición GET y obtener la respuesta
        String responseBody = mockMvc.perform(get("/api/citas/{animalId}", 1L))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Imprimir la respuesta para depurar
        System.out.println("Response Body: " + responseBody);

    }

    @Test
    public void testGuardarCitaVeterinaria() throws Exception {
        // Preparar el DTO de solicitud (request)
        CitaVeterinariaRequestDto requestDto = new CitaVeterinariaRequestDto();
        requestDto.setFechaCita(LocalDateTime.now().plusDays(1));
        requestDto.setVeterinario("Dr. Smith");
        requestDto.setAnimalId(1L);

        // Preparar la entidad CitaVeterinaria que será devuelta por el servicio
        Animal animal = new Animal();
        animal.setId(1L);
        animal.setNombre("Fido");

        CitaVeterinaria citaVeterinaria = new CitaVeterinaria();
        citaVeterinaria.setId(1L);
        citaVeterinaria.setFechaCita(requestDto.getFechaCita());
        citaVeterinaria.setVeterinario(requestDto.getVeterinario());
        citaVeterinaria.setAnimal(animal);
        citaVeterinaria.setEstado(EstadoCita.PENDIENTE);

        // Simular el comportamiento del servicio para guardar la cita
        when(citaVeterinariaService.guardarCita(any(CitaVeterinariaRequestDto.class))).thenReturn(citaVeterinaria);

        // Realizar la petición POST y verificar la respuesta
        mockMvc.perform(post("/api/citas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))  // Convertir el DTO de solicitud a JSON
                .andExpect(status().isOk());  // Esperar 200 OK en lugar de 201 Created

    }
}










