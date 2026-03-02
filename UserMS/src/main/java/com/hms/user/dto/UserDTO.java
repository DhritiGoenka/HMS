package com.hms.user.dto;

import com.hms.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{6,15}$",
            message = "Password should be between 6 characters to 15 characters long and contain atleast 1 uppercase, 1 lowercase, 1 digit and 1 special character")
    private String password;
    private Roles role;
    private Long profileId;

    public User toEntity(){
        return new User(
                this.id,
                this.name,
                this.email,
                this.password,
                this.role,
                this.profileId
        );
    }

}
