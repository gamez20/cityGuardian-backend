package co.edu.uniquindio.cityguardian.mapping.mappers;
import co.edu.uniquindio.cityguardian.mapping.dto.CreateUserDto;
import co.edu.uniquindio.cityguardian.mapping.dto.UserDto;
import co.edu.uniquindio.cityguardian.model.User;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", constant = "CLIENT")
    @Mapping(target = "isActive", constant = "false")
    @Mapping(target = "registerDate", expression = "java(java.time.LocalDateTime.now())")
    User toDocument(CreateUserDto userDTO);

    CreateUserDto toDTO(User user);

    UserDto toUserDto(User user);

    default String map(ObjectId value) {
        return value != null ? value.toString() : null;
    }

    default ObjectId map(String value) {
        return value != null ? new ObjectId(value) : null;
    }

}