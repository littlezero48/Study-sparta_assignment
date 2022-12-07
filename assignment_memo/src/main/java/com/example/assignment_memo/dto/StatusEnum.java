package com.example.assignment_memo.dto;

import lombok.Getter;

@Getter
public enum StatusEnum {

    //200
    OK(200, "OK"),

    //400
    BAD_REQUEST(400, "BAD_REQUEST"),
    BAD_REQUEST_TOKEN(400, "토큰이 유효하지 않습니다."),
    NO_ACCESS(400, "작성자만 삭제/수정할 수 있습니다."),
    EXIST_USER(400, "중복된 username입니다."),
    LOGIN_MATCH_FAIL(400, "회원을 찾을 수 없습니다."),
    NOT_FOUND(404, "NOT_FOUND"),

    //500
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR");

    int statusCode;
    String code;

    StatusEnum(int statusCode, String code){
        this.statusCode = statusCode;
        this.code = code;
    }
}
