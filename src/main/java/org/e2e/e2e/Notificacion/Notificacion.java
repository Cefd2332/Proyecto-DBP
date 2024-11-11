package org.e2e.e2e.Notificacion;

import jakarta.persistence.*;
import org.e2e.e2e.Usuario.Usuario;
import java.time.LocalDateTime;

@Entity
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensaje;

    private LocalDateTime fechaEnvio = LocalDateTime.now();

    private boolean enviada = false;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    // Constructores
    public Notificacion() {
    }

    public Notificacion(String mensaje, Usuario usuario) {
        this.mensaje = mensaje;
        this.usuario = usuario;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Notificacion{" +
                "id=" + id +
                ", mensaje='" + mensaje + '\'' +
                ", fechaEnvio=" + fechaEnvio +
                ", enviada=" + enviada +
                ", usuarioId=" + (usuario != null ? usuario.getId() : null) +
                '}';
    }
}
