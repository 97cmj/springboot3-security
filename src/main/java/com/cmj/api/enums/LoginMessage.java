package com.cmj.api.enums;

import lombok.Getter;

@Getter
public enum LoginMessage {

    //이메일은 빈 값이 될 수 없습니다.
    EMAIL_IS_EMPTY("이메일은 빈 값이 될 수 없습니다."),
    //이메일 형식이 올바르지 않습니다.
    EMAIL_IS_NOT_VALID("이메일 형식이 올바르지 않습니다."),

    //패스워드는 빈 값이 될 수 없습니다.
    PASSWORD_IS_EMPTY("패스워드는 빈 값이 될 수 없습니다."),

    //패스워드는 최소 8자 이상이어야 합니다.
    PASSWORD_IS_NOT_VALID("패스워드는 최소 8자 이상이어야 합니다."),

    //패스워드는 최소 하나의 숫자와 특수문자를 포함해야 합니다.
    PASSWORD_IS_NOT_VALID2("패스워드는 최소 하나의 숫자와 특수문자를 포함해야 합니다.");

    private final String message;

    LoginMessage(String message) {
        this.message = message;
    }



}
