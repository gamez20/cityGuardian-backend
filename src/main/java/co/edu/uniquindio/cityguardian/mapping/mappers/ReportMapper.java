package co.edu.uniquindio.cityguardian.mapping.mappers;

import co.edu.uniquindio.cityguardian.mapping.dto.CreateReportDto;
import co.edu.uniquindio.cityguardian.mapping.dto.ReportDTO;
import co.edu.uniquindio.cityguardian.model.Report;
import co.edu.uniquindio.cityguardian.model.ReportStatus;
import co.edu.uniquindio.cityguardian.model.Category;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(ReportStatus.CREATED)")
    @Mapping(target = "solved", constant = "false")
    @Mapping(target = "important", constant = "false")
    @Mapping(target = "creationDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "priority", constant = "0")
    @Mapping(target = "categoryId", source = "categoryId")
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
