package org.e2e.e2e.Notificacion;

import jakarta.persistence.*;
import lombok.Data;
import org.e2e.e2e.Usuario.Usuario;

import java.time.LocalDateTime;

@Data
@Entity
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensaje;
    private LocalDateTime fechaEnvio;
    private boolean enviada;  // Para marcar si ya se envió la notificación

    @ManyToOne
    @JoinColumn(name = "usuario_id")  // Relaciona la notificación con el usuario adoptante
    private Usuario usuario;
}
