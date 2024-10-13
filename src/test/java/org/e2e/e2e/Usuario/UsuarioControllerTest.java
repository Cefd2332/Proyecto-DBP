package org.e2e.e2e.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test // DEBE SALIR PORQUE NO ES ADMIN!
    void testGuardarUsuario() throws Exception {
        // Crear un DTO de solicitud
        UsuarioRequestDto requestDto = new UsuarioRequestDto();
        requestDto.setNombre("Carlos");
        requestDto.setEmail("carlos@example.com");
        requestDto.setDireccion("Lima, Perú");

        // Crear un objeto de Usuario simulado que será el tipo que se espera en el servicio
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Carlos");
        usuario.setEmail("carlos@example.com");
        usuario.setDireccion("Lima, Perú");

        // Simular el comportamiento del servicio
        when(usuarioService.guardarUsuario(any(UsuarioRequestDto.class))).thenReturn(usuario);

        // Realizar una petición POST y validar la respuesta
        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Carlos"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("carlos@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.direccion").value("Lima, Perú"));
    }
}


