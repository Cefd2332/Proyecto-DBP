package org.e2e.e2e.Notificacion;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class NotificacionPushService {

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
            System.out.println("Notificación enviada exitosamente: " + response);
        } catch (Exception e) {
            System.err.println("Error al enviar la notificación: " + e.getMessage());
        }
    }
}
