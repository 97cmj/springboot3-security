package com.cmj.security.dto;

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

    private String email;

    private String password;
}
