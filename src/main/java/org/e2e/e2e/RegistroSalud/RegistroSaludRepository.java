package org.e2e.e2e.RegistroSalud;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface RegistroSaludRepository extends JpaRepository<RegistroSalud, Long> {
    boolean existsByFechaConsultaAndAnimalId(@NotNull(message = "La fecha de la consulta no puede ser nula") @PastOrPresent(message = "La fecha de la consulta no puede ser en el futuro") LocalDate fechaConsulta, Long id);
}
