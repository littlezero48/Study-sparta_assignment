package com.example.assignment_createapi_test.controller;

import com.example.assignment_createapi_test.dto.MemberResponseDto;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController // @ResponsBody + @Controller
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member")  // 전체조회 요청
    public List<MemberResponseDto> getMember(){
        return memberService.getMember();
    }

    @GetMapping("/member/{id}") // 하나 아이디를 기준 조회
    public MemberResponseDto getMember(Long id){
        return memberService.getMember(id);
    }
}
