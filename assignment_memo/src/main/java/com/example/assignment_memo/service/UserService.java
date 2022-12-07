package com.example.assignment_memo.service;

import com.example.assignment_memo.dto.LoginRequestDto;
import com.example.assignment_memo.dto.MessageDto;
import com.example.assignment_memo.dto.SignupRequestDto;
import com.example.assignment_memo.dto.StatusEnum;
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

    public MessageDto signup(SignupRequestDto dto){
        String username = dto.getUsername();
        String password = dto.getPassword();
        UserRoleEnum role = ADMIN_TOKEN.equals(dto.getAdminToken()) ? UserRoleEnum.ADMIN : UserRoleEnum.USER ;

        Optional<User> existUser = userRepository.findByUsername(username);
        if(existUser.isPresent()){
//            throw new IllegalArgumentException("중복된 사용자가 존재합니다");
            return new MessageDto(StatusEnum.EXIST_USER);
        }

        User user = new User(username, password, role);
        userRepository.save(user);

        return new MessageDto(StatusEnum.OK);
    }

    public MessageDto login(LoginRequestDto dto, HttpServletResponse response){
        String username = dto.getUsername();
        String password = dto.getPassword();

//        User user = userRepository.findByUsernameAndPassword(username, password).orElseThrow(
//                ()-> new IllegalArgumentException("사용자가 없습니다.")
//        );

        Optional<User> user = userRepository.findByUsernameAndPassword(username, password);
        if (user.isEmpty()) {
            return new MessageDto(StatusEnum.LOGIN_MATCH_FAIL);
        }

        // HTTP Header에 생성한 토큰을 붙여 보내기!
        //Optional은 값이 없을 때 값을 쓰려고 하면 예외 발생
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.get().getUsername(),  user.get().getRole()));  // 메소드사용하려면 의존성주입 먼저

        return new MessageDto(StatusEnum.OK);
    }
}