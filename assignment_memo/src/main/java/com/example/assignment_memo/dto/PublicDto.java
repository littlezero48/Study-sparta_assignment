package com.example.assignment_memo.dto;

import lombok.Getter;

@Getter     // Class 모든 필드의 Getter method를 생성
public class PublicDto {

    private int result;
    private String message;

    public void setResult(int result, String message){
        this.result = result;
        this.message = message;
    }

}
