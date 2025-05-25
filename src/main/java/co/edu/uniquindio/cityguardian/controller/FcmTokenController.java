package co.edu.uniquindio.cityguardian.controller;

import co.edu.uniquindio.cityguardian.mapping.dto.FcmTokenDto;
import co.edu.uniquindio.cityguardian.mapping.dto.MessageDTO;
import co.edu.uniquindio.cityguardian.services.FcmTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fcm")
@RequiredArgsConstructor
public class FcmTokenController {

    private final FcmTokenService fcmTokenService;

    @PostMapping("/token")
    public ResponseEntity<MessageDTO<String>> saveToken(
            @Valid @RequestBody FcmTokenDto tokenDto,
            @RequestHeader("Authorization") String token) {
        try {
            String userId = extractUserIdFromToken(token);
            fcmTokenService.saveToken(userId, tokenDto.token());
            return ResponseEntity.ok(new MessageDTO<>(false, "Token FCM guardado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageDTO<>(true, "Error al guardar el token FCM: " + e.getMessage()));
        }
    }

    @DeleteMapping("/token")
    public ResponseEntity<MessageDTO<String>> deleteToken(
            @RequestHeader("Authorization") String token) {
        try {
            String userId = extractUserIdFromToken(token);
            fcmTokenService.deleteToken(userId);
            return ResponseEntity.ok(new MessageDTO<>(false, "Token FCM eliminado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageDTO<>(true, "Error al eliminar el token FCM: " + e.getMessage()));
        }
    }

    private String extractUserIdFromToken(String token) {
        // Implementa la lógica para extraer el userId del token JWT
        // Ejemplo básico:
        return token.replace("Bearer ", "").split("\\.")[1];
    }
}