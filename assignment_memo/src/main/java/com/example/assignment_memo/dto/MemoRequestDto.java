package com.example.assignment_memo.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter // Class 모든 필드의 Getter method를 생성
public class MemoRequestDto extends PublicDto { // Dto를 통해 클라이언트에서 데이터가 오고 서버로부터 데이터가 나가는 객체
    private Long id;
    private String title;       // Dto는 항상 private로 접근 제한하는 건가?
    private String username;
    private String password;    // Entity에 @JsonIgnore 처리되어있음
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    
}
