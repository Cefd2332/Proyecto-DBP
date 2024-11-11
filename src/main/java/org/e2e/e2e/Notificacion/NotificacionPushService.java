package org.e2e.e2e.Notificacion;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificacionPushService {

    private static final Logger logger = LoggerFactory.getLogger(NotificacionPushService.class);

    public void enviarNotificacion(String token, String titulo, String cuerpo) {
        Message message = Message.builder()
                .setToken(token)
                .setNotification(Notification.builder()
                        .setTitle(titulo)
                        .setBody(cuerpo)
                        .build())
                .build();

        // Enviar la notificación a Firebase
        try {
            String response = FirebaseMessaging.getInstance().send(message);
            logger.info("Notificación enviada exitosamente: {}", response);
        } catch (Exception e) {
            logger.error("Error al enviar la notificación: {}", e.getMessage());
            // Aquí puedes lanzar una excepción personalizada si es necesario
        }
    }
}
