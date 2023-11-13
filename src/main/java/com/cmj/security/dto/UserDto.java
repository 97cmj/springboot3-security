package com.cmj.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Email cannot be null, empty or blank")
    @Email
    @Pattern(regexp = ".+@.+\\..{2,}", message = "Invalid email address")
    private String email;

    @NotBlank(message = "Password cannot be null, empty or blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}$", message = "Password must contain at least one digit and one special character")
    private String password;
}
