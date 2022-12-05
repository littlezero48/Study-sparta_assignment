package com.example.assignment_memo.service;

import com.example.assignment_memo.dto.LoginRequestDto;
import com.example.assignment_memo.dto.PublicDto;
import com.example.assignment_memo.dto.SignupRequestDto;
import com.example.assignment_memo.entity.User;
import com.example.assignment_memo.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public PublicDto signup(SignupRequestDto dto){
        String username = dto.getUsername();
        String password = dto.getPassword();
        PublicDto exportDto = new PublicDto();

        Optional<User> existUser = userRepository.findByUsername(username);
        if(existUser.isPresent()){
            exportDto.setResult(200, "중복된 사용자가 존재합니다");
            return exportDto;
        }

        User user = new User(username, password);
        userRepository.save(user);

        exportDto.setResult(200, "가입에 성공했습니다.");
        return exportDto;
    }

    public PublicDto login(LoginRequestDto dto, HttpServletResponse header){
        String username = dto.getUsername();
        String password = dto.getPassword();
        PublicDto exportDto = new PublicDto();

        User user = userRepository.findByUsername(username).orElseThrow(
                ()-> new IllegalArgumentException("사용자가 없습니다.")
        );

        if(!password.equals(user.getPassword())){
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

//        header.addHeader(); 여기서 추가해야함

        exportDto.setResult(200, "로그인에 성공했습니다.");
        return exportDto;
    }
}
