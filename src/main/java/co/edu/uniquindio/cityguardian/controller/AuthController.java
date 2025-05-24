package co.edu.uniquindio.cityguardian.controller;

import co.edu.uniquindio.cityguardian.mapping.dto.CreateUserDto;
import co.edu.uniquindio.cityguardian.mapping.dto.MessageDTO;
import co.edu.uniquindio.cityguardian.model.dto.AuthResponseDTO;
import co.edu.uniquindio.cityguardian.model.dto.LoginRequest;
import co.edu.uniquindio.cityguardian.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private  UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
        try {
            return ResponseEntity.ok(userService.login(loginRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageDTO<>(true, e.getMessage()));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageDTO<String>> createNewUser(@Valid @RequestBody CreateUserDto account) throws Exception {
        try {
            userService.createNewUser(account);
            return ResponseEntity.status(201).body(new MessageDTO<>(false, "Su registro ha sido exitoso"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageDTO<>(true, e.getMessage()));
        }
    }
} 