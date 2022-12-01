package com.example.assignment_createapi_test.dto;

import com.example.assignment_createapi_test.entity.Member;
import lombok.Getter;

@Getter
public class MemberResponseDto {
    private Long id;
    private String name;
    private String email;
    private String pw;

    public MemberResponseDto(Member member){ // 생성자
        this.id = member.getId();
        this.name = member.getName();
        this.email = member.getEmail();
        this.pw = member.getPw();
    }
}
