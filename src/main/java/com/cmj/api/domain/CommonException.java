package com.cmj.api.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class CommonException extends RuntimeException{
    private String msg;
    private HttpStatus status;
}