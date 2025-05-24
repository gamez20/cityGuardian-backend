package co.edu.uniquindio.cityguardian.mapping.dto;

import co.edu.uniquindio.cityguardian.model.dto.LocationDTO;
import org.springframework.data.annotation.Id;

public record ReportDTO(

        @Id
        String id,
        String title,
        String category,
        String description,
        String solved,
        String important,
        LocationDTO location,
        String status,
        String creationDate,
        String clientIdFk,
        String priority
) {
}
