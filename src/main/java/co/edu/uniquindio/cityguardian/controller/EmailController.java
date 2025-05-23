package co.edu.uniquindio.cityguardian.controller;

import co.edu.uniquindio.cityguardian.dto.EmailDTO;
import co.edu.uniquindio.cityguardian.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/enviar")
    public ResponseEntity<String> enviarEmail(@RequestBody EmailDTO emailDTO) {
        try {
            emailService.enviarEmail(emailDTO);
            return ResponseEntity.ok("Correo enviado correctamente");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al enviar el correo: " + e.getMessage());
        }
    }
}