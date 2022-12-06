package com.example.assignment_memo.service;

import com.example.assignment_memo.dto.LoginRequestDto;
import com.example.assignment_memo.dto.PublicDto;
import com.example.assignment_memo.dto.SignupRequestDto;
import com.example.assignment_memo.entity.User;
import com.example.assignment_memo.entity.UserRoleEnum;
import com.example.assignment_memo.jwt.JwtUtil;
import com.example.assignment_memo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    private static final String ADMIN_TOKEN = "admin";

    public PublicDto signup(SignupRequestDto dto){
        String username = dto.getUsername();
        String password = dto.getPassword();
        UserRoleEnum role = ADMIN_TOKEN.equals(dto.getAdminCode()) ? UserRoleEnum.ADMIN : UserRoleEnum.USER ;
        PublicDto exportDto = new PublicDto();

        Optional<User> existUser = userRepository.findByUsername(username);
        if(existUser.isPresent()){
            throw new IllegalArgumentException("중복된 사용자가 존재합니다");
        }

        User user = new User(username, password, role);
        userRepository.save(user);

        exportDto.setResult(200, "가입에 성공했습니다.");
        return exportDto;
    }

    public PublicDto login(LoginRequestDto dto, HttpServletResponse response){
        String username = dto.getUsername();
        String password = dto.getPassword();
        PublicDto exportDto = new PublicDto();

        User user = userRepository.findByUsername(username).orElseThrow(
                ()-> new IllegalArgumentException("사용자가 없습니다.")
        );

        if(!password.equals(user.getPassword())){
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        // HTTP Header에 생성한 토큰을 붙여 보내기!
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(),  user.getRole()));  // 메소드사용하려면 의존성주입 먼저

        exportDto.setResult(200, "로그인에 성공했습니다.");
        return exportDto;
    }
}