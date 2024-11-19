package org.e2e.e2e.Animal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    // Buscar animales por adoptante que estén activos
    @Query("SELECT a FROM Animal a WHERE a.adoptante.id = :adoptanteId AND a.estadoRegistro = 'ACTIVO'")
    List<Animal> findActiveByAdoptanteId(@Param("adoptanteId") Long adoptanteId);

    // Buscar animales eliminados
    @Query("SELECT a FROM Animal a WHERE a.estadoRegistro = 'ELIMINADO'")
    List<Animal> findAnimalesEliminados();

    /**
     * Encuentra todos los animales asociados a un adoptante específico.
     *
     * @param adoptanteId El ID del adoptante (usuario).
     * @return Lista de Animal asociados al adoptante.
     */
    List<Animal> findByAdoptanteId(Long adoptanteId);
}
