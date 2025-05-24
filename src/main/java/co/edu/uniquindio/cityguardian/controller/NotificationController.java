package co.edu.uniquindio.cityguardian.controller;

import co.edu.uniquindio.cityguardian.dto.NotificationRequestDTO;
import co.edu.uniquindio.cityguardian.dto.TopicNotificationRequestDTO;
import co.edu.uniquindio.cityguardian.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    @Autowired
    private  NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationRequestDTO request) {
        try {
            String response = notificationService.sendNotification(
                    request.getToken(),
                    request.getTitle(),
                    request.getBody()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/topic")
    public ResponseEntity<String> sendNotificationToTopic(@RequestBody TopicNotificationRequestDTO request) {
        try {
            String response = notificationService.sendNotificationToTopic(
                    request.getTopic(),
                    request.getTitle(),
                    request.getBody()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}