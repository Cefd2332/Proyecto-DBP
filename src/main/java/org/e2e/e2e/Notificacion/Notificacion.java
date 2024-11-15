package org.e2e.e2e.Notificacion;

import jakarta.persistence.*;
import org.e2e.e2e.Adoptante.Adoptante;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones") // Opcional: Especifica el nombre de la tabla
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensaje;

    private LocalDateTime fechaEnvio = LocalDateTime.now();

    private boolean enviada = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adoptante_id", nullable = false)
    private Adoptante adoptante; // Cambiado de Usuario a Adoptante

    // Constructores

    /**
     * Constructor por defecto.
     */
    public Notificacion() {
    }

    /**
     * Constructor parametrizado.
     *
     * @param mensaje    Mensaje de la notificación.
     * @param adoptante  Adoptante asociado a la notificación.
     */
    public Notificacion(String mensaje, Adoptante adoptante) {
        this.mensaje = mensaje;
        this.adoptante = adoptante;
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    // No se proporciona setId ya que generalmente el ID es generado automáticamente

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public boolean isEnviada() {
        return enviada;
    }

    public void setEnviada(boolean enviada) {
        this.enviada = enviada;
    }

    public Adoptante getAdoptante() {
        return adoptante;
    }

    public void setAdoptante(Adoptante adoptante) {
        this.adoptante = adoptante;
    }

    // Método toString

    @Override
    public String toString() {
        return "Notificacion{" +
                "id=" + id +
                ", mensaje='" + mensaje + '\'' +
                ", fechaEnvio=" + fechaEnvio +
                ", enviada=" + enviada +
                ", adoptanteId=" + (adoptante != null ? adoptante.getId() : null) +
                '}';
    }
}
