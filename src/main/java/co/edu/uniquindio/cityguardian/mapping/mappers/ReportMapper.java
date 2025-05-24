package co.edu.uniquindio.cityguardian.mapping.mappers;


import co.edu.uniquindio.cityguardian.mapping.dto.CreateReportDto;
import co.edu.uniquindio.cityguardian.mapping.dto.ReportDTO;
import co.edu.uniquindio.cityguardian.model.Report;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    @Mapping(target = "status", constant = "CREATED")
    @Mapping(target = "category", constant = "WITHOUT_CATEGORY")
    @Mapping(target = "solved", constant = "false")
    @Mapping(target = "important", constant = "false")
    @Mapping(target = "creationDate", expression = "java(java.time.ZonedDateTime.now(java.time.ZoneId.of(\"America/Bogota\")).toLocalDateTime())")
    Report toDocument(CreateReportDto reportDto);

    CreateReportDto toDTO(Report report);

    ReportDTO toReportDto(Report report);

    default String map(ObjectId value) {
        return value != null ? value.toString() : null;
    }

    default ObjectId map(String value) {
        return value != null ? new ObjectId(value) : null;
    }

}
