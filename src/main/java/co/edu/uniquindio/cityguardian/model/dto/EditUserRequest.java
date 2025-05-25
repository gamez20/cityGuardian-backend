package co.edu.uniquindio.cityguardian.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record EditUserRequest(

        @NotBlank @Length(max = 100) String name,
        @NotBlank @Length(max = 100) String lastName,
        @Length(max = 10) String phone,
        @NotBlank @Length(max = 200)  String address,
        @NotBlank @Length(max = 100)  String city

) {
}
