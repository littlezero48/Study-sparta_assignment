package com.example.assignment_memo.config;

import com.example.assignment_memo.util.jwt.JwtUtil;
import com.example.assignment_memo.util.jwt.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


//Spring Security는 Bean 인터페이스나 구현 클래스에서 대상 매서드에 다음의 어노테이션을 선언함으로 매서드 호출을 보안
@Configuration                                                                                                          // 설정파일을 만들기 위한 어노테이션 또는 Bean을 등록하기 위한 어노테이션
@RequiredArgsConstructor
@EnableWebSecurity                                                                                                      //@EnableGlobalMethodSecurity 스프링 시큐리티의 메소드 어노테이션 기반 시큐리티를 활성화
@EnableGlobalMethodSecurity(securedEnabled = true)                                                                      //securedEnabled = @Secured어노테이션 활성화 여부 @Secured는 접근 권한 부여하는 어노테이션
public class WebSecurityConfig {
    private final JwtUtil jwtUtil;                                                                                      // 의존성 주입

    // 스프링 시큐리티에서 제공하는 인터페이스 객체로 비밀번호를 암호화
    @Bean                                                                                                               // 의존성 주입으로 사용할 수 있게 Bean으로 등록 / 설정을 애플리케이션 전역에 사용
    public PasswordEncoder passwordEncoder(){                                                                           // PasswordEncorder 객체 반환하는 메소드
        return new BCryptPasswordEncoder();                                                                             //password Encorder을 쓸때 BCryptPasswordEncorder을 사용
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){                                                               //접근 허용 설정
        return (web) -> web.ignoring()                                                                                  // 보안 필터에서 제외할 것
                .requestMatchers(PathRequest.toH2Console())                                                             // 요청이 일치할때 : 경로요청, H2로의
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());                                  // 요청일치 : 경로요청, 스트링부트에서 제공하는 정적 리소스로의

    }

    @Bean
    // **** SecurityFilterChain : Spring 보안 Filter를 경정하는 데 사용되는 Filter
    // Spring Security는 요청이 들어오면 Servlet FilterChain을 자동으로 구성한 후 거치게 한다.
    // FilterChain은 여러 Filter를 chain형태로 묶어놓은 것을 의미
    // Filter는 Client 요청이 전달되기 전후의 URL 패턴에 맞는 모든 요청에 필터링
    // CSRF, XSS 등의 보안 검사를 통해 올바른 요청이 아닐 경우 이를 차단
    // ***** HttpSecurity : 스프링시큐리티의 거의 대부분설정을 담당 하는 객체
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // CSRF (Cross Site Request Forgery / 크로스 사이트 요청 위조) 정상적인 사용자가 의도치 않은 위조요청
        // 이걸 방지하는 건데 왜 disable이냐하면 restAPI에서는 필요한 요청을 위해 필요한 인증정보를 포함시켜야해
        // 서버가 인증정보를 저장하지 않기 때문에 굳이 불필요한 csrf코드를 삭정할 필요가 없기 때문
        http.csrf().disable();

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        // 스프링 시큐리티 세션 정책 설정 메소드
        // STATELESS : 스프링시큐리티가 생성하지도않고 기존것을 사용하지도 않음
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests() // http의 권한 요청이
                .antMatchers("/api/user/**").permitAll()                                                    // 이 특정 경로라면 (** 하위모든) 모두 인증 절차 없이 permit
                .antMatchers("/api/memos").permitAll()
                .anyRequest() // 설정한 경로 외의 모든 경로들은
                .authenticated() // 인증된 사용자만이 접근가능
                .and()                                                                                                  // 다른 기능을 사용하고자 하면 and로 나누는 역할
                // ***** addFilterBefore(A,B) B직전에 A필터가 걸리도록 한다
                // ***** UsernamePasswordAuthenticationFilter 는 AbstractAuthenticationProcessingFilter를 상속한 Filter다.
                // 기본적으로 Form Login 기반을 사용할 때 username 과 password 확인하여 인증
                .addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();                                                                                            //빌더한 것을 리턴
    }
}
