package co.edu.uniquindio.cityguardian.mapping.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import java.util.List;

public record CreateReportDto(
        @NotBlank @Length(max = 50) String title,
        @NotBlank @Length(max = 300) String description,
        @NotBlank String status,
        @NotBlank String category,
        List<String> imageUrls
) {
}
