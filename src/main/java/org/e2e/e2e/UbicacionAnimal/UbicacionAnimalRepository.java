package org.e2e.e2e.UbicacionAnimal;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UbicacionAnimalRepository extends JpaRepository<UbicacionAnimal, Long> {
    boolean existsByLatitudAndLongitudAndAnimalId(@NotNull(message = "La latitud no puede ser nula") Double latitud, @NotNull(message = "La longitud no puede ser nula") Double longitud, Long id);
}
