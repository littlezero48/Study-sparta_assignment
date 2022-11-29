package com.example.assignment_memo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter // Class 모든 필드의 Setter method를 생성
@Getter
public class PublicDto {

    private String result;
    private String message;

    public void setResult(String result, String massage){
        this.result = result;
        this.message = massage;
    }
}
