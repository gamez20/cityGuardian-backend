package co.edu.uniquindio.cityguardian.mapping.mappers;

import co.edu.uniquindio.cityguardian.mapping.dto.CategoryDTO;
import co.edu.uniquindio.cityguardian.model.Category;
import co.edu.uniquindio.cityguardian.model.dto.CreateReportRequest;
import co.edu.uniquindio.cityguardian.mapping.dto.ReportDTO;
import co.edu.uniquindio.cityguardian.model.Report;
import co.edu.uniquindio.cityguardian.model.Location;
import co.edu.uniquindio.cityguardian.model.dto.LocationDTO;
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
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "location", source = "location")
    Report toDocument(CreateReportRequest reportDto);

    @Mapping(target = "location", source = "location")
    CreateReportRequest toDTO(Report report);

    ReportDTO toReportDto(Report report);

    default String map(ObjectId value) {
        return value != null ? value.toString() : null;
    }

    default ObjectId map(String value) {
        return value != null ? new ObjectId(value) : null;
    }

    default Location map(LocationDTO locationDTO) {
        if (locationDTO == null)
            return null;
        return new Location(locationDTO.latitude(), locationDTO.longitude());
    }

    default LocationDTO map(Location location) {
        if (location == null)
            return null;
        return new LocationDTO(location.getLatitude(), location.getLongitude());
    }

    default CategoryDTO map(Category category) {
        return new CategoryDTO(category.getId(), category.getName(), category.getDescription());
    }

    default Category map(CategoryDTO categoryDTO) {
        if (categoryDTO == null)
            return null;
        Category category = new Category();
        category.setId(categoryDTO.id());
        category.setName(categoryDTO.name());
        category.setDescription(categoryDTO.description());
        return category;
    }
}
