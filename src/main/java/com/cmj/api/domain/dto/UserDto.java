package com.cmj.api.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "이메일은 빈 값이 될 수 없습니다.")
    @Email
    @Pattern(regexp = ".+@.+\\..{2,}", message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "패스워드는 빈 값이 될 수 없습니다.")
    @Size(min = 8, message = "패스워드는 최소 8자 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}$", message = "패스워드는 최소 하나의 숫자와 특수문자를 포함해야 합니다.")
    private String password;
}
