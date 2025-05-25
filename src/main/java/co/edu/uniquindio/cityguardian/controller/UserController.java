package co.edu.uniquindio.cityguardian.controller;

import co.edu.uniquindio.cityguardian.dto.UserReportsDTO;
import co.edu.uniquindio.cityguardian.model.dto.EditUserRequest;
import co.edu.uniquindio.cityguardian.mapping.dto.MessageDTO;
import co.edu.uniquindio.cityguardian.mapping.dto.UserDto;
import co.edu.uniquindio.cityguardian.model.dto.SendCodeRequest;
import co.edu.uniquindio.cityguardian.model.dto.VerificationCodeByEmailRequest;
import co.edu.uniquindio.cityguardian.model.dto.VerificationCodeRequest;
import co.edu.uniquindio.cityguardian.services.UserService;
import co.edu.uniquindio.cityguardian.utils.TokenUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PatchMapping("/update")
    public ResponseEntity<MessageDTO<String>> edit(@Valid @RequestBody EditUserRequest account) throws Exception {
        try {
            userService.updateUser(account);
            return ResponseEntity.ok(new MessageDTO<>(false,"Usuario Actualizado Correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageDTO<>(true, e.getMessage()));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<MessageDTO<String>> delete() throws Exception {
        try {
            String email = TokenUtils.getEmailFromToken();
            userService.deleteUser(email);
            return ResponseEntity.status(200).body(new MessageDTO<>(false, "Usuario eliminado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageDTO<>(true, e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable String id) throws Exception {
        return userService.getUserById(id);
    }

    @GetMapping("/buscar")
    public ResponseEntity<UserDto> buscarPorEmail(@RequestParam String email) {
        try {
            UserDto usuario = userService.getUserByEmail(email);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getProfile() {
        try {
            String email = TokenUtils.getEmailFromToken();
            UserDto usuario = userService.getUserByEmail(email);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/all")
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @PostMapping("/sendCode")
    public ResponseEntity<MessageDTO<String>> sendCode() throws Exception {
        try {
            String email = TokenUtils.getEmailFromToken();
            if (email == null) {
                throw new AuthenticationException("Usuario sin email registrado");
            }
            return userService.sendVerificationCode(email);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageDTO<>(true, e.getMessage()));
        }
    }

    @PostMapping("/verifyCode")
    public ResponseEntity<MessageDTO<String>> verifyCode(@Valid @RequestBody VerificationCodeRequest request)
            throws Exception {
        try {
            String email = TokenUtils.getEmailFromToken();
            if (email == null) {
                throw new AuthenticationException("Usuario sin email registrado");
            }
            return userService.verifyCode(email, request.code());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageDTO<>(true, e.getMessage()));
        }
    }

    @GetMapping("/myReports")
    public ResponseEntity<UserReportsDTO> getMyReports() throws Exception {
        try {
            String email = TokenUtils.getEmailFromToken();
            if (email == null) {
                throw new AuthenticationException("Usuario sin email registrado");
            }
            return ResponseEntity.ok(userService.getUserReports(email));
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los reportes del usuario: " + e.getMessage());
        }
    }
}
