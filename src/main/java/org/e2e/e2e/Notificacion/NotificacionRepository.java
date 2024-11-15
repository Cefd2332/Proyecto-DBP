package org.e2e.e2e.Notificacion;

import org.e2e.e2e.Adoptante.Adoptante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByAdoptante(Adoptante adoptante);
}
