package org.e2e.e2e.CitaVeterinaria;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface CitaVeterinariaRepository extends JpaRepository<CitaVeterinaria, Long> {
    boolean existsByFechaCitaAndVeterinario(@NotNull(message = "La fecha de la cita no puede ser nula") @Future(message = "La fecha de la cita debe estar en el futuro") LocalDateTime fechaCita, @NotBlank(message = "El veterinario no puede estar vacío") String veterinario);
}