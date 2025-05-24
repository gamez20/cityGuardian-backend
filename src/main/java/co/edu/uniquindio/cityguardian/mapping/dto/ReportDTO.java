package co.edu.uniquindio.cityguardian.mapping.dto;

import org.springframework.data.annotation.Id;

public record ReportDTO(

        @Id
        String id,
        String title,
        String category,
        String description,
        String solved,
        String important,
        String locationIdFk,
        String status,
        String creationDate,
        String clientIdFk,
        String priority
) {
}
