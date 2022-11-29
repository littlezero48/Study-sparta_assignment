package com.example.assignment_memo.dto;

import lombok.Getter;

@Getter     // Class 모든 필드의 Getter method를 생성
public class PublicDto {

    private String result;
    private String message;

    public void setResult(String result, String massage){
        this.result = result;
        this.message = massage;
    }

}
