package co.edu.uniquindio.cityguardian.model.dto;

public record ChangePasswordRequest(
    String email,
    String newPassword
) {}
