package co.edu.uniquindio.cityguardian.controller;

import co.edu.uniquindio.cityguardian.mapping.dto.CreateUserDto;
import co.edu.uniquindio.cityguardian.mapping.dto.EditUserDto;
import co.edu.uniquindio.cityguardian.mapping.dto.MessageDTO;
import co.edu.uniquindio.cityguardian.mapping.dto.UserDto;
import co.edu.uniquindio.cityguardian.model.dto.AuthResponseDTO;
import co.edu.uniquindio.cityguardian.model.dto.LoginRequest;
import co.edu.uniquindio.cityguardian.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController  {

    @Autowired
    private UserService userService;

    @PatchMapping
    public UserDto edit (@Valid @RequestBody EditUserDto account) throws Exception {
        return userService.updateUser(account);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDTO<String>> delete (@PathVariable String id) throws  Exception{
        userService.deleteUser(id);
        return ResponseEntity.status(200).body(new MessageDTO<>(false, "Usuario eliminado exitosamente"));
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable String id) throws Exception{
        return userService.getUserById(id);
    }

    @GetMapping("/all")
    public List<UserDto> getUsers(){
        return userService.getUsers();
    }


}
