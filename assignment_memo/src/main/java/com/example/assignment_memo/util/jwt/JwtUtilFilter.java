package com.example.assignment_memo.util.jwt;

import com.example.assignment_memo.util.ApiResponse.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.assignment_memo.util.ApiResponse.CodeError.INVALID_TOKEN;


@Slf4j // 로그를 위한 어노테이션
@RequiredArgsConstructor
//커스텀 Security 필터
public class JwtUtilFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil; //의존성 주입해 bean사용

    @Override
    // 토큰 유효 판별하는 공통 메소드
    // 여기서 httpServlet의 요청과 응답을 다 사용.
    // **** FilterChain : 요청(Request)과 응답(Response)에 대한 정보들을 변경할 수 있게 개발자들에게 제공하는 서블린 컨테이너
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtUtil.resolveToken(request);   // 받아온 요청에서 토큰을 찾아 문자열 변수에 받음

        if(token != null) { // 토큰이 없으면 인증 처리 않고 다음으로
            if(!jwtUtil.validateToken(token)){  // 유효한 토큰인지 판별
                jwtExceptionHandler(response, "Token Error", HttpStatus.UNAUTHORIZED.value()); // 유효하지 않으면 클라이언트가 권한이 없다 알린다.
                return;  //#####
            }
            Claims info = jwtUtil.getUserInfoFromToken(token);  // token의 정보 조각인 클레임을 가져옴
            setAuthentication(info.getSubject());   // 정보 subject안에 있는 내용을 토대로 SecurityContextHolder 안 SecurityContext에 인증객체을 넣어줌
        }
        filterChain.doFilter(request,response); //왜 필터 체인이냐 하면 앞 뒤로 필터를 연결하고 있기 때문 이 함수로는 다음 필터로 넘길수 있다.
        // 인증 객체를 넣은 채 넘어가면 Security 쪽이 인지를 하고 Controller까지 요청이 넘어간다. 그 객체를 가지고 갖은 서비스에 인증된 객체를 넘긴다.
    }

    public void setAuthentication(String username) {
        // SecurityContextHolder 시큐리티가 인증한 내용들을 가지고 있어 SecurityContext를 포함하고 있고 SecurityContext의 현재 스레드와 연결해 주는 역할
        // SecurityContext는 인터페이스다, SecurityContextHolder를 통해 얻을 수 있으며 Authentication을 가지고 있고, 이에 대한 getter, setter가 정의되어 있다.
        // Authentication는 인터페이스이며 AuthenticationManager.authenticate(Authentication)에 의해 인증된 principal 또는 token을 나타낸다.
        // Authentication는 principal, credentials, authorities를 가지고 있으며 이 3가지를 통해 확인 가능
        // * principal : user 식별 정보, UserDetailsService에 의해 반환된 UserDetails의 instance
        // * credentials : 자격 증명, 올바른 주체인지 증명
        // * authorities : user에게 부여된 권한
        SecurityContext context = SecurityContextHolder.createEmptyContext();       // 빈 컨텍스트를 만들어 대입
        Authentication authentication = jwtUtil.createAuthentication(username);     // 인증객체를 만드는걸 jwtUtil로 책임분리 - 인증객체 생성후
        context.setAuthentication(authentication); // 컨텍스트 안에 세팅

        SecurityContextHolder.setContext(context); // 홀더에 컨텍스트를 세팅
        // SecurityContextHolder는 안에서부터 princiapl, credetials, authorities를 가진 authentication이 있고,
        // 이authentication를 SecurityContext가 가지고 있으며, 또 이 SecurityContext를 SecurityContextHolder가 가지고 있다.
    }

    public void jwtExceptionHandler(HttpServletResponse response, String msg, int statusCode) { // 토큰이 오류가 나면 메세지를 커스터 마이징해 Client로 보냄
        response.setStatus(statusCode);                 // 코드 설정
        response.setContentType("application/json");    // reponse 타입 설정
        try {
            String json = new ObjectMapper().writeValueAsString(new CustomException(INVALID_TOKEN)); // 자바 객체로 부터 objecMapper을 통해 이를 문자열 혹은 byte 배열로 반환
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
