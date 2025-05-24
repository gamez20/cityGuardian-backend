package co.edu.uniquindio.cityguardian.services.impl;

import co.edu.uniquindio.cityguardian.services.NotificationService;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    @Override
    public String sendNotification(String token, String title, String body) throws Exception {
        try {
            // Validar token
            if (token == null || token.trim().isEmpty()) {
                throw new IllegalArgumentException("El token FCM no puede estar vacío");
            }

            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .putData("click_action", "ANGULAR_NOTIFICATION_CLICK")
                    .putData("url", "/dashboard") // Ruta de Angular para navegación
                    .putData("type", "web_notification")
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            return "Notificación enviada con éxito: " + response;

        } catch (FirebaseMessagingException e) {
            if (e.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED) {
                return "El token FCM ha expirado o ha sido desregistrado";
            } else if (e.getMessagingErrorCode() == MessagingErrorCode.INVALID_ARGUMENT) {
                return "Token FCM inválido";
            }
            throw new Exception("Error al enviar notificación: " + e.getMessage());
        }
    }

    @Override
    public String sendNotificationToTopic(String topic, String title, String body) throws Exception {
        try {
            if (topic == null || topic.trim().isEmpty()) {
                throw new IllegalArgumentException("El topic no puede estar vacío");
            }

            Message message = Message.builder()
                    .setTopic(topic)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .putData("click_action", "ANGULAR_NOTIFICATION_CLICK")
                    .putData("url", "/dashboard")
                    .putData("type", "web_notification")
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            return "Notificación enviada al topic con éxito: " + response;
        } catch (FirebaseMessagingException e) {
            throw new Exception("Error al enviar notificación al topic: " + e.getMessage());
        }
    }
}