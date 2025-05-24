package co.edu.uniquindio.cityguardian.mapping.dto;

import co.edu.uniquindio.cityguardian.model.dto.LocationDTO;
import co.edu.uniquindio.cityguardian.model.ReportStatus;
import java.time.LocalDateTime;
import java.util.List;

public record ReportDTO(
    String id,
    String title,
    String description,
    Boolean solved,
    Boolean important,
    String categoryId,
    ReportStatus status,
    LocalDateTime creationDate,
    List<String> comments,
    int priority,
    String userId,
    List<String> imageUrls,
    LocationDTO location,
    String rejectReason
) {
}
