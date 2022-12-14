package com.example.assignment_memo.service;

import com.example.assignment_memo.dto.LoginRequestDto;
import com.example.assignment_memo.dto.MessageDto;
import com.example.assignment_memo.dto.SignupRequestDto;
import com.example.assignment_memo.dto.StatusEnum;
import com.example.assignment_memo.entity.User;
import com.example.assignment_memo.entity.UserRoleEnum;
import com.example.assignment_memo.repository.UserRepository;
import com.example.assignment_memo.util.ApiResponse.CustomException;
import com.example.assignment_memo.util.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

import static com.example.assignment_memo.util.ApiResponse.CodeError.EXIST_USER;
import static com.example.assignment_memo.util.ApiResponse.CodeError.LOGIN_MATCH_FAIL;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 가입시 작성하면 관리자로 인정 받는 코드
    private static final String ADMIN_TOKEN = "admin";

    // 회원가입
    public MessageDto signup(SignupRequestDto dto){

        // 1. 응답으로부터 가입 정보 받기
        String username = dto.getUsername();
        String password = dto.getPassword();

        // 2. 권한 체크
        UserRoleEnum role = ADMIN_TOKEN.equals(dto.getAdminToken()) ? UserRoleEnum.ADMIN : UserRoleEnum.USER ;

        // 3. 동일한 유저 존재여부 체크
        if(userRepository.findByUsername(username).isPresent()){
            throw new CustomException(EXIST_USER);
        };

        // 4. 가입 정보 저장
        User user = new User(username, password, role);
        userRepository.save(user);

        // 5. 성공 메세지 리턴
        return new MessageDto(StatusEnum.OK);
    }

    // 로그인
    public MessageDto login(LoginRequestDto dto, HttpServletResponse response){

        // 1. 응답으로부터 로그인 정보 받기
        String username = dto.getUsername();
        String password = dto.getPassword();

        // 2. 아이디와 비밀번호 일치여부 확인
        User user = userRepository.findByUsernameAndPassword(username, password).orElseThrow(
                ()-> new CustomException(LOGIN_MATCH_FAIL)
        );

        // 3. 응답 헤더에 토큰 세팅
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(),  user.getRole()));     // HTTP Header에 생성한 토큰을 세팅 //Optional은 값이 없을 때 값을 쓰려고 하면 예외 발생

        // 4. 성공 메세지 리턴
        return new MessageDto(StatusEnum.OK);
    }
}