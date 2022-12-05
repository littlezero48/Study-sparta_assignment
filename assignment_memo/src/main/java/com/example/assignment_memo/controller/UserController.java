package com.example.assignment_memo.controller;

import com.example.assignment_memo.dto.LoginRequestDto;
import com.example.assignment_memo.dto.PublicDto;
import com.example.assignment_memo.dto.SignupRequestDto;
import com.example.assignment_memo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RequestMapping("/api/user")
@RestController // @ResponseBody로 ㅣ
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public PublicDto signup(@RequestBody SignupRequestDto dto){
        return userService.signup(dto);
    }

    @PostMapping("/login")
    public PublicDto login(@RequestBody LoginRequestDto dto, HttpServletResponse header){
        return userService.login(dto, header);
    }
}
