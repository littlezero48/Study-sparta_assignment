package com.example.assignment_memo.dto;

import lombok.Getter;

import java.util.List;

@Getter     // Class 모든 필드의 Getter method를 생성
public class MessageDto {

    private int status;
    private String message;
    private Object data;

    public MessageDto(StatusEnum status){
        this.status = status.getStatusCode();
        this.message = status.getCode();
    }

    public MessageDto(StatusEnum status, MemoResponseDto dto){
        this.status = status.getStatusCode();
        this.message = status.getCode();
        this.data = dto;    //Object 클래스에 다 들어가나?
    }

    public MessageDto(StatusEnum status, ReplyResponseDto dto){
        this.status = status.getStatusCode();
        this.message = status.getCode();
        this.data = dto;    //Object 클래스에 다 들어가나?
    }


    public MessageDto(StatusEnum status, List<MemoResponseDto> dto){
        this.status = status.statusCode;
        this.message = status.code;
        this.data = dto;    //Object 클래스에 다 들어가나?
    }
}
