package org.e2e.e2e.Animal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    /**
     * Encuentra todos los animales asociados a un adoptante específico.
     *
     * @param adoptanteId El ID del adoptante (usuario).
     * @return Lista de Animal asociados al adoptante.
     */
    List<Animal> findByAdoptanteId(Long adoptanteId);
}
