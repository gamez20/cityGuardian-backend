package co.edu.uniquindio.cityguardian.services.impl;

import co.edu.uniquindio.cityguardian.dto.EmailDTO;
import co.edu.uniquindio.cityguardian.dto.UserReportsDTO;
import co.edu.uniquindio.cityguardian.exceptions.RepeatedElementException;
import co.edu.uniquindio.cityguardian.mapping.dto.CreateUserDto;
import co.edu.uniquindio.cityguardian.model.dto.EditUserRequest;
import co.edu.uniquindio.cityguardian.mapping.dto.MessageDTO;
import co.edu.uniquindio.cityguardian.mapping.dto.ReportDTO;
import co.edu.uniquindio.cityguardian.mapping.dto.UserDto;
import co.edu.uniquindio.cityguardian.mapping.mappers.UserMapper;
import co.edu.uniquindio.cityguardian.model.User;
import co.edu.uniquindio.cityguardian.model.dto.AuthResponseDTO;
import co.edu.uniquindio.cityguardian.model.dto.LoginRequest;
import co.edu.uniquindio.cityguardian.repository.UserRepository;
import co.edu.uniquindio.cityguardian.security.JWTUtils;
import co.edu.uniquindio.cityguardian.services.EmailService;
import co.edu.uniquindio.cityguardian.services.ReportService;
import co.edu.uniquindio.cityguardian.services.UserService;
import co.edu.uniquindio.cityguardian.utils.TokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ReportService reportService;

    @Override
    public AuthResponseDTO login(LoginRequest loginRequest) throws Exception {
        if (!emailExist(loginRequest.email())) {
            throw new AuthenticationException("El email no existe");
        }
        User user = repository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new AuthenticationException("El usuario no existe"));

        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new Exception("Credenciales inválidas");
        }

        Map<String, String> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole().name());

        String token = jwtUtils.generateToken(user.getId(), claims);

        UserDto userDto = userMapper.toUserDto(user);

        return new AuthResponseDTO(token, userDto);
    }

    @Override
    public UserDto getUserByEmail(String email) throws Exception {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }

        User user = repository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("Usuario no encontrado"));

        return userMapper.toUserDto(user);
    }

    @Override
    public void createNewUser(CreateUserDto userDto) throws Exception {

        if (emailExist(userDto.email())) {
            throw new RepeatedElementException("El email ya está registrado");
        }

        User user = userMapper.toDocument(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
    }

    public boolean emailExist(String email) {
        return repository.findByEmail(email).isPresent();
    }

    @Override
    public UserDto updateUser(EditUserRequest updatedUser) throws Exception {
        String email = TokenUtils.getEmailFromToken();
        if (email == null) {
            throw new AuthenticationException("Usuario sin email registrado");
        }

        Optional<User> optionalUser = repository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }

        User existingUser = optionalUser.get();

        objectMapper.updateValue(existingUser, updatedUser);

        return userMapper.toUserDto(repository.save(existingUser));
    }

    @Override
    public void deleteUser(String id) throws Exception {
        Optional<User> reportOptional = repository.findById(id);
        if (reportOptional.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }
        repository.deleteById(id);
    }

    @Override
    public UserDto getUserById(String id) throws Exception {
        Optional<User> optionalUser = repository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }
        return userMapper.toUserDto(optionalUser.get());
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> users = repository.findAll();
        return users.stream().map(userMapper::toUserDto).toList();
    }

    @Override
    public ResponseEntity<MessageDTO<String>> sendVerificationCode(String email) throws Exception {
        try {
            if (!emailExist(email)) {
                throw new AuthenticationException("El email no existe");
            }

            String verificationCode = String.format("%06d", new Random().nextInt(1000000));

            User user = repository.findByEmail(email)
                    .orElseThrow(() -> new AuthenticationException("Usuario no encontrado"));

            user.setVerificationCode(verificationCode);
            user.setVerificationCodeExpiry(LocalDateTime.now().plusMinutes(15));
            repository.save(user);
            EmailDTO emailDTO = getEmailDTO(user, verificationCode);
            emailService.enviarEmail(emailDTO);

            return ResponseEntity.ok(new MessageDTO<>(false, "Código de verificación enviado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageDTO<>(true, e.getMessage()));
        }
    }

    @NotNull
    private static EmailDTO getEmailDTO(User user, String verificationCode) {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setAsunto("\uD83D\uDD10 Verifica tu cuenta en CityGuardian");
        emailDTO.setCuerpo(
                "<p>Hola, " + user.getName() + "</p>" +
                        "<p>Gracias por registrarte en <strong>CityGuardian</strong>, la plataforma para reportar incidentes de inseguridad en tu ciudad y mantenerte informado.</p>"
                        +
                        "<p>Para completar la verificación de tu cuenta, ingresa el siguiente código en el sitio web:</p>"
                        +
                        "<h1 style='font-size: 28px; font-weight: bold;'>" + verificationCode + "</h1>" +
                        "<p>Este código expirará en 15 minutos.</p>" +
                        "<p>Gracias por ayudarnos a construir ciudades más seguras.<br><strong>– El equipo de CityGuardian</strong></p>");
        emailDTO.setDestinatario(user.getEmail());
        return emailDTO;
    }

    @Override
    public ResponseEntity<MessageDTO<String>> verifyCode(String email, String code) throws Exception {
        try {
            User user = repository.findByEmail(email)
                    .orElseThrow(() -> new AuthenticationException("Usuario no encontrado"));

            if (user.getVerificationCode() == null) {
                throw new AuthenticationException("No hay código de verificación pendiente");
            }

            if (user.getVerificationCodeExpiry().isBefore(LocalDateTime.now())) {
                throw new AuthenticationException("El código de verificación ha expirado");
            }

            if (!user.getVerificationCode().equals(code)) {
                throw new AuthenticationException("Código de verificación inválido");
            }

            user.setVerificationCode(null);
            user.setVerificationCodeExpiry(null);
            user.setIsActive(true);
            repository.save(user);

            return ResponseEntity.ok(new MessageDTO<>(false, "Código verificado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageDTO<>(true, e.getMessage()));
        }
    }

    @Override
    public UserReportsDTO getUserReports(String email) throws Exception {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("Usuario no encontrado"));

        List<ReportDTO> reports = new ArrayList<>();

        if (user.getReportIds() != null && !user.getReportIds().isEmpty()) {
            reports = user.getReportIds().stream()
                    .map(reportId -> {
                        try {
                            return reportService.getReportById(reportId);
                        } catch (Exception e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        return new UserReportsDTO(
                user.getId(),
                user.getName(),
                reports);
    }

    @Override
    public ResponseEntity<MessageDTO<String>> changePassword(String email, String newPassword) throws Exception {
        try {

            User user = repository.findByEmail(email)
                    .orElseThrow(() -> new AuthenticationException("Usuario no encontrado"));

            if (newPassword == null || newPassword.trim().isEmpty()) {
                throw new IllegalArgumentException("La nueva contraseña no puede estar vacía");
            }

            String encodedPassword = passwordEncoder.encode(newPassword);

            user.setPassword(encodedPassword);
            repository.save(user);

            return ResponseEntity.ok(new MessageDTO<>(false, "Contraseña actualizada exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageDTO<>(true, e.getMessage()));
        }
    }
}
