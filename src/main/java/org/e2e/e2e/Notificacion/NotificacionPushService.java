package org.e2e.e2e.Notificacion;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.e2e.e2e.Adoptante.Adoptante;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificacionPushService {

    private static final Logger logger = LoggerFactory.getLogger(NotificacionPushService.class);

    public void enviarNotificacion(Adoptante adoptante, String titulo, String cuerpo) {
        String deviceToken = adoptante.getDeviceToken();
        if (deviceToken == null || deviceToken.isEmpty()) {
            logger.warn("El adoptante con ID {} no tiene un token de dispositivo registrado.", adoptante.getId());
            return; // O lanza una excepción si lo prefieres
        }

        Message message = Message.builder()
                .setToken(deviceToken)
                .setNotification(Notification.builder()
                        .setTitle(titulo)
                        .setBody(cuerpo)
                        .build())
                .build();

        // Enviar la notificación a Firebase
        try {
            String response = FirebaseMessaging.getInstance().send(message);
            logger.info("Notificación enviada exitosamente a Adoptante ID {}: {}", adoptante.getId(), response);
        } catch (Exception e) {
            logger.error("Error al enviar la notificación a Adoptante ID {}: {}", adoptante.getId(), e.getMessage());
            // Aquí puedes lanzar una excepción personalizada si es necesario
        }
    }
}
