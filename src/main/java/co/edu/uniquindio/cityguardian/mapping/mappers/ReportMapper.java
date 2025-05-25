package co.edu.uniquindio.cityguardian.mapping.mappers;

import co.edu.uniquindio.cityguardian.mapping.dto.CategoryDTO;
import co.edu.uniquindio.cityguardian.mapping.dto.CommentDto;
import co.edu.uniquindio.cityguardian.model.Category;
import co.edu.uniquindio.cityguardian.model.Comment;
import co.edu.uniquindio.cityguardian.model.dto.CreateReportRequest;
import co.edu.uniquindio.cityguardian.mapping.dto.ReportDTO;
import co.edu.uniquindio.cityguardian.model.Report;
import co.edu.uniquindio.cityguardian.model.Location;
import co.edu.uniquindio.cityguardian.model.dto.LocationDTO;
import org.bson.types.ObjectId;
import java.time.LocalDateTime; // Cambio aquí
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(ReportStatus.CREATED)")
    @Mapping(target = "solved", constant = "false")
    @Mapping(target = "important", constant = "false")
    @Mapping(target = "creationDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "comments", expression = "java(new java.util.ArrayList<>())")
    @Mapping(target = "priority", constant = "0")
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "location", source = "location")
    Report toDocument(CreateReportRequest reportDto);

    @Mapping(target = "location", source = "location")
    CreateReportRequest toDTO(Report report);

    @Mapping(target = "comments", expression = "java(toCommentDtoList(report.getComments()))")
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

    default CommentDto toCommentDto(Comment comment) {
        if (comment == null) return null;
        return new CommentDto(
                comment.getId(),
                comment.getUserId(),
                comment.getFirstName(),
                comment.getLastName(),
                comment.getDate(),
                comment.getMessage()
        );
    }

    default Comment toComment(CommentDto dto) {
        if (dto == null) return null;
        Comment comment = new Comment();
        comment.setId(dto.id());
        comment.setUserId(dto.userId());
        comment.setFirstName(dto.firstName());
        comment.setLastName(dto.lastName());
        comment.setDate(dto.date());
        comment.setMessage(dto.message());
        return comment;
    }

    default Comment mapStringToComment(String message) {
        if (message == null) return null;
        Comment comment = new Comment();
        comment.setId(UUID.randomUUID().toString());
        comment.setMessage(message);
        comment.setDate(LocalDateTime.now().toString());
        return comment;
    }

    default List<CommentDto> toCommentDtoList(List<Comment> comments) {
        if (comments == null) return null;
        return comments.stream()
                .map(this::toCommentDto)
                .collect(Collectors.toList());
    }

    default List<Comment> toCommentList(List<CommentDto> dtos) {
        if (dtos == null) return null;
        return dtos.stream()
                .map(this::toComment)
                .collect(Collectors.toList());
    }
}