package com.example.assignment_memo.util.ApiResponse;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApiResult {
    private final LocalDateTime timestamp =  LocalDateTime.now();
    private final int status;
    private final String message;
    private final Object response;
    private final CodeError codeError;

    // 응답으로 반환될 최종 객체
    public ApiResult(CodeSuccess codeSuccess, Object response, CodeError codeError){

        // 성공 코드 존재시 성공코드를 대입, 부존재시 에러코드를 대입
        if (codeSuccess != null){
            this.status = codeSuccess.getHttpStatus().value();                                                          // CodeSuccess Enum에서 해당 상태 코드를 int로 받는다
            this.message = codeSuccess.getDetail();                                                                     // CodeSuccess Enum에서 해당 상태 내용을 String으로 받는다.
        } else {
            this.status = codeError.getHttpStatus().value();                                                            // CodeError Enum
            this.message = codeError.getDetail();
        }
        this.response = response;                                                                                       // 성공했을 시 반환 데이터
        this.codeError = codeError;
    }
}