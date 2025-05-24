package co.edu.uniquindio.cityguardian.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("users")
@Setter
@Getter
@NoArgsConstructor
public class User {

    @Id
    private String id;
    private String name;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String password;
    private Boolean isActive;
    private UserRol role;
    private LocalDateTime registerDate;
    private String verificationCode;
    private LocalDateTime verificationCodeExpiry;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        this.isActive = active;
    }

    public UserRol getRole() {
        return role;
    }

    public void setRole(UserRol role) {
        this.role = role;
    }

    public LocalDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDateTime registerDate) {
        this.registerDate = registerDate;
    }

    public String getVerificationCode() {return verificationCode; }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public LocalDateTime getVerificationCodeExpiry() {
        return verificationCodeExpiry;
    }
    
    public void setVerificationCodeExpiry(LocalDateTime verificationCodeExpiry) {
        this.verificationCodeExpiry = verificationCodeExpiry;
    }

    @Builder
    public User(String name, String city, String address, String email, String phone, String password, UserRol role, Boolean isActive, LocalDateTime registerDate, LocalDateTime verificationCodeExpiry, String verificationCode) {
        this.name = name;
        this.city = city;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
        this.isActive = isActive;
        this.registerDate = registerDate;
        this.verificationCode = verificationCode;
        this.verificationCodeExpiry = verificationCodeExpiry;
    }

}
