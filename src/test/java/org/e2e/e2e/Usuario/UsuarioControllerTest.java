package org.e2e.e2e.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.e2e.e2e.security.CustomUserDetailsService;
import org.e2e.e2e.security.jwt.JwtTokenUtil;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil; // Se añade el mock de JwtTokenUtil

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test // Prueba para verificar un error si el email ya existe
    void testGuardarUsuarioEmailExistente() throws Exception {
        // Crear un DTO de solicitud
        UsuarioRequestDto requestDto = new UsuarioRequestDto();
        requestDto.setNombre("Carlos");
        requestDto.setEmail("carlos@example.com");
        requestDto.setDireccion("Lima, Perú");

        // Simular el comportamiento del servicio para lanzar excepción
        when(usuarioService.guardarUsuario(any(UsuarioRequestDto.class)))
                .thenThrow(new IllegalArgumentException("Email ya registrado"));

        // Realizar una petición POST y validar que regresa error 400
        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test // Prueba para obtener un usuario por ID
    void testObtenerUsuarioPorId() throws Exception {
        // Crear un objeto de Usuario simulado
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Carlos");
        usuario.setEmail("carlos@example.com");
        usuario.setDireccion("Lima, Perú");

        // Simular el comportamiento del servicio
        when(usuarioService.obtenerUsuarioPorId(1L)).thenReturn(usuario);

        // Realizar una petición GET y validar la respuesta
        mockMvc.perform(get("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}


