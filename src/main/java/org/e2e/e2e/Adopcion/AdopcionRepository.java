package org.e2e.e2e.Adopcion;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdopcionRepository extends JpaRepository<Adopcion, Long> {

    boolean existsByAnimalId(Long animalId);
}
