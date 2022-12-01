package com.example.assignment_createapi_test.controller;

import com.example.assignment_createapi_test.dto.MemberResponseDto;
import com.example.assignment_createapi_test.service.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // @ResponsBody + @Controller
@RequiredArgsConstructor    // 필수정보를 생성하는 어노테이션
public class MemberController {

    private final MemberService memberService;  // 서비스 연결

    @GetMapping("/member/{id}") // 하나 아이디를 기준 조회
    public MemberResponseDto findMember(@PathVariable Long id){ // 경로값 받아 조회
        return memberService.getMemberInfo(id);
    }

    @GetMapping("/member")  // 전체조회 요청
    public List<MemberResponseDto> findAllMember(){ // Request로 받아야 할 값 없음
        return memberService.getMemberList();
    }
}
