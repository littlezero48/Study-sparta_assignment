package com.example.assignment_memo.controller;

import com.example.assignment_memo.dto.LoginRequestDto;
import com.example.assignment_memo.dto.MessageDto;
import com.example.assignment_memo.dto.SignupRequestDto;
import com.example.assignment_memo.service.UserService;
import com.example.assignment_memo.util.ApiResponse.ApiResult;
import com.example.assignment_memo.util.ApiResponse.ApiUtil;
import com.example.assignment_memo.util.ApiResponse.CodeSuccess;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequestMapping("/api/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ApiResult signup(@RequestBody @Valid SignupRequestDto dto){
        MessageDto messageDto = userService.signup(dto);
        return ApiUtil.successResponse(CodeSuccess.JOIN_OK, messageDto);
    }

    @PostMapping("/login")
    public ApiResult login(@RequestBody LoginRequestDto dto, HttpServletResponse response){
        MessageDto messageDto = userService.login(dto, response);
        return ApiUtil.successResponse(CodeSuccess.JOIN_OK, messageDto);
    }
}
