package co.edu.uniquindio.cityguardian.mapping.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryDTO(
    String id,
    @NotBlank String name,
    @NotBlank String description
) {}
