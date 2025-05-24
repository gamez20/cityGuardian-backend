package co.edu.uniquindio.cityguardian.services;

import co.edu.uniquindio.cityguardian.mapping.dto.CreateUserDto;
import co.edu.uniquindio.cityguardian.mapping.dto.EditUserDto;
import co.edu.uniquindio.cityguardian.mapping.dto.MessageDTO;
import co.edu.uniquindio.cityguardian.mapping.dto.UserDto;
import co.edu.uniquindio.cityguardian.model.dto.AuthResponseDTO;
import co.edu.uniquindio.cityguardian.model.dto.LoginRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    AuthResponseDTO login (LoginRequest loginRequest) throws Exception;
    void createNewUser(CreateUserDto userDto) throws Exception;
    UserDto updateUser(EditUserDto updatedUser) throws Exception;
    void deleteUser(String id) throws Exception;
    UserDto getUserById(String id) throws Exception;
    List<UserDto> getUsers();
    ResponseEntity<MessageDTO<String>> sendVerificationCode(String email) throws Exception;
}
