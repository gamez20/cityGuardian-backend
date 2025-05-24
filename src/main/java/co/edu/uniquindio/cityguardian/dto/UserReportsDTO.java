package co.edu.uniquindio.cityguardian.dto;

import java.util.List;

import co.edu.uniquindio.cityguardian.mapping.dto.ReportDTO;

public record UserReportsDTO(
    String userId,
    String userName,
    List<ReportDTO> reports
) {}
