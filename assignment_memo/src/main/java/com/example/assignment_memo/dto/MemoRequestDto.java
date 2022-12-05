package com.example.assignment_memo.dto;

import lombok.Getter;

@Getter // Class 모든 필드의 Getter method를 생성
public class MemoRequestDto { // Dto를 통해 클라이언트에서 데이터가 오고 서버로부터 데이터가 나가는 객체

    private String title;       // Dto는 항상 private로 접근 제한하는 건가?
    private String content;
    
}
