package co.edu.uniquindio.cityguardian.controller;

import co.edu.uniquindio.cityguardian.mapping.dto.CreateUserDto;
import co.edu.uniquindio.cityguardian.mapping.dto.MessageDTO;
import co.edu.uniquindio.cityguardian.model.dto.ChangePasswordRequest;
import co.edu.uniquindio.cityguardian.model.dto.LoginRequest;
import co.edu.uniquindio.cityguardian.model.dto.SendCodeRequest;
import co.edu.uniquindio.cityguardian.model.dto.VerificationCodeByEmailRequest;
import co.edu.uniquindio.cityguardian.services.UserService;
import co.edu.uniquindio.cityguardian.utils.TokenUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

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
    public ResponseEntity<MessageDTO<String>> createNewUser(@Valid @RequestBody CreateUserDto account)
            throws Exception {
        try {
            userService.createNewUser(account);
            return ResponseEntity.status(201).body(new MessageDTO<>(false, "Su registro ha sido exitoso"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageDTO<>(true, e.getMessage()));
        }
    }

    @PostMapping("/changePassword")
    public ResponseEntity<MessageDTO<String>> changePassword(@Valid @RequestBody ChangePasswordRequest request)
            throws Exception {
        try {
            String email = request.email();
            if (email == null) {
                throw new AuthenticationException("Usuario sin email registrado");
            }
            return userService.changePassword(email, request.newPassword());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageDTO<>(true, e.getMessage()));
        }
    }

    @PostMapping("/sendCodeByEmail")
    public ResponseEntity<MessageDTO<String>> sendCode(@Valid @RequestBody SendCodeRequest request) throws Exception {
        try {
            return userService.sendVerificationCode(request.email());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageDTO<>(true, e.getMessage()));
        }
    }

    @PostMapping("/verifyCodeByEmail")
    public ResponseEntity<MessageDTO<String>> verifyCode(@Valid @RequestBody VerificationCodeByEmailRequest request)
            throws Exception {
        try {
            return userService.verifyCode(request.email(), request.code());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageDTO<>(true, e.getMessage()));
        }
    }
}