package com.example.assignment_memo.util.ApiResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CodeSuccess {

    OK(HttpStatus.OK, "OK"),
    JOIN_OK(HttpStatus.OK, "회원가입에 성공했습니다"),
    LOGIN_OK(HttpStatus.OK, "로그인에 성공했습니다"),
    GET_OK(HttpStatus.OK, "조회 성공했습니다"),
    CREATE_OK(HttpStatus.OK, "생성 성공했습니다"),
    MODIFY_OK(HttpStatus.OK, "수정 성공했습니다"),
    DELETE_OK(HttpStatus.OK, "삭제 성공했습니다"),
    ;

    private final HttpStatus httpStatus;
    private final String detail;
}
