package org.e2e.e2e.Usuario;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void testFindByEmail() {
        // Guardar un usuario en la base de datos
        Usuario usuario = new Usuario();
        usuario.setNombre("Carlos Flores");
        usuario.setEmail("carlos.flores@utec.edu.pe");
        usuario.setPassword("password123");
        usuarioRepository.save(usuario);

        // Buscar por email
        Optional<Usuario> foundUsuario = usuarioRepository.findByEmail("carlos.flores@utec.edu.pe");

        // Validar
        Assertions.assertTrue(foundUsuario.isPresent());
        Assertions.assertEquals("Carlos Flores", foundUsuario.get().getNombre());
    }
}

