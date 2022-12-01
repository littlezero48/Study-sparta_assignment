package com.example.assignment_createapi_test.service;

import com.example.assignment_createapi_test.dto.MemberResponseDto;
import com.example.assignment_createapi_test.entity.Member;
import com.example.assignment_createapi_test.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service    // 서비스 선언
@RequiredArgsConstructor    // 필수정보 생성자 자동 생성
public class MemberService {

    private final MemberRepository memberRepository;    // 레포지토리 연결

    // 기능 : id 기준으로 하나의 회원 정보만 조회
    public MemberResponseDto getMemberInfo(Long id){
        Member member = memberRepository.findById(id).orElseThrow(()-> new NullPointerException("회원 상세 조회 실패"));    // 조회 실패시 예외처리
        return new MemberResponseDto(member);   // Entity를 Dto로 변환
    }

    // 기능 : 전체 회원 조회
    public List<MemberResponseDto> getMemberList() {
        List<Member> resultMember = memberRepository.findAll(); // 모두 조회, 정렬조건은 별도로 요구된게 없었음
        List<MemberResponseDto> exportDto = new ArrayList<>();
        for(Member member : resultMember){                      // Entity리스트를 Dto리스트로 변환하는 작업
            MemberResponseDto tempDto = new MemberResponseDto(member);
            exportDto.add(tempDto);
        }
        return exportDto;   // Dto리스트를 리턴
    }
}
