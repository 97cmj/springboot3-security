package com.cmj.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Response<T> {
    private String status;  //success, fail 구분
    private int code;    //api 고유 상태코드
    private String msg;     //반환 메세지
    private T data;         //반환 데이터

}