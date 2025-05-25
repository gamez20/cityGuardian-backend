package co.edu.uniquindio.cityguardian.model.dto;

public record VerificationCodeByEmailRequest(
        String email,
        String code
) {
}
