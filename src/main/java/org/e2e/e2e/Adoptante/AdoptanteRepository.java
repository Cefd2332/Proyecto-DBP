package org.e2e.e2e.Adoptante;

import org.e2e.e2e.exceptions.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdoptanteRepository extends JpaRepository<Adoptante, Long> {

    @Query("SELECT a FROM Adoptante a WHERE a.usuario.id = :usuarioId")
    List<Adoptante> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT a FROM Adoptante a WHERE a.estado = 'ELIMINADO'")
    List<Adoptante> findAdoptantesEliminados();

    /**
     * Encuentra un Adoptante por su dirección de correo electrónico.
     *
     * @param email La dirección de correo electrónico del adoptante.
     * @return Un Optional que contiene el adoptante si se encuentra, o vacío si no.
     */
    Optional<Adoptante> findByEmail(String email);

    /**
     * Verifica si existe un Adoptante con la dirección de correo electrónico proporcionada.
     *
     * @param email La dirección de correo electrónico a verificar.
     * @return true si existe un adoptante con el email dado, de lo contrario false.
     */
    boolean existsByEmail(String email);

    default Adoptante obtenerAdoptanteById(Long id) {
        return findById(id)
                .orElseThrow(() -> new NotFoundException("Adoptante no encontrado con ID: " + id));
    }

    /**
     * Encuentra todos los Adoptantes que tienen un rol específico.
     *
     * @param rol El rol a buscar.
     * @return Una lista de adoptantes que poseen el rol especificado.
     */
    List<Adoptante> findByRolesContaining(String rol);

    /**
     * Encuentra Adoptantes cuyo nombre contiene una cadena específica.
     *
     * @param nombre La cadena a buscar en los nombres.
     * @return Una lista de adoptantes que cumplen con el criterio.
     */
    @Query("SELECT a FROM Adoptante a WHERE a.nombre LIKE %:nombre%")
    List<Adoptante> buscarPorNombre(@Param("nombre") String nombre);
}


