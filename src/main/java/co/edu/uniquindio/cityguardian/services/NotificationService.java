package co.edu.uniquindio.cityguardian.services;



public interface NotificationService {
    String sendNotification(String token, String title, String body) throws Exception;
    String sendNotificationToTopic(String topic, String title, String body) throws Exception;
}