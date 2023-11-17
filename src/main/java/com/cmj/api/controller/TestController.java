package com.cmj.api.controller;


import com.cmj.api.domain.CommonException;
import com.cmj.api.domain.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/v1/test")
    public ResponseEntity<Object> test() {

        Response<Object> result = Response.builder()
                .status("success")
                .code(200)
                .data("test")
                .msg("성공")
                .build();

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @GetMapping("/v1/error")
    public ResponseEntity<Object> error() {

        Response<Object> result = Response.builder()
                .status("success")
                .code(200)
                .data("test")
                .msg("성공")
                .build();

        throw new CommonException("내가 만든 exception", HttpStatus.FOUND);
    }
}
