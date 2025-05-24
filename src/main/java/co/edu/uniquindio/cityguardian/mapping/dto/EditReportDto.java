package co.edu.uniquindio.cityguardian.mapping.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import java.util.List;

public record EditReportDto(

        @NotBlank @Length(max = 25) String title,
        @NotBlank @Length(max = 300) String description,
        @NotBlank String categoryId,
        List<String> imageUrls


) {
}
