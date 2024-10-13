package org.e2e.e2e.Usuario;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.times;

@SpringBootTest
public class UsuarioServiceTest {

    @MockBean
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Test
    public void testGuardarUsuario() {
        // Datos simulados
        Usuario usuario = new Usuario();
        usuario.setNombre("Carlos Flores");
        usuario.setEmail("carlos.flores@utec.edu.pe");

        UsuarioRequestDto requestDto = new UsuarioRequestDto();
        requestDto.setNombre("Carlos Flores");
        requestDto.setEmail("carlos.flores@utec.edu.pe");

        // Simulación de lo que devolvería el repositorio
        Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

        // Ejecución del servicio
        Usuario response = usuarioService.guardarUsuario(requestDto);  // Cambiado a Usuario

        // Validaciones
        Assertions.assertNotNull(response);
        Assertions.assertEquals("Carlos Flores", response.getNombre());
        Assertions.assertEquals("carlos.flores@utec.edu.pe", response.getEmail());

        // Verificar que el método del repositorio fue llamado
        Mockito.verify(usuarioRepository, times(1)).save(Mockito.any(Usuario.class));
    }
}


