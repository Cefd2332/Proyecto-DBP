package org.e2e.e2e.Vacuna;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;

public interface VacunaRepository extends JpaRepository<Vacuna, Long> {

    // Consulta para buscar vacunas cuya fecha de aplicación esté dentro de los próximos 7 días
    @Query("SELECT v FROM Vacuna v WHERE v.fechaAplicacion BETWEEN :today AND :nextWeek")
    List<Vacuna> findVacunasProximas(LocalDate today, LocalDate nextWeek);
}
