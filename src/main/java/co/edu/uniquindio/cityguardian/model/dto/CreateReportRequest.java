package co.edu.uniquindio.cityguardian.model.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record CreateReportRequest(
                @NotBlank @Length(max = 50) String title,
                @NotBlank @Length(max = 300) String description,
                @NotBlank String status,
                List<String> imageUrls,
                @NotBlank String categoryId,
                LocationDTO location) {
}