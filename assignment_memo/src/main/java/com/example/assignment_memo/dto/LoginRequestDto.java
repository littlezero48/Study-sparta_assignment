package com.example.assignment_memo.dto;

import lombok.Getter;

@Getter
public class LoginRequestDto extends PublicDto {
    private String username;
    private String password;
}
