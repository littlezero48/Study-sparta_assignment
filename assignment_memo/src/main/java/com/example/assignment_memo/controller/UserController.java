package com.example.assignment_memo.controller;

import com.example.assignment_memo.dto.PublicDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public PublicDto signup(@RequestBody SignupRequestDto dto){
        return new userService.signup(dto);
    }

    @PostMapping("/login")
    public PublicDto login(@RequestBody LoginRequestDto dtd){
        return new userService.login(dto);
    }
}
