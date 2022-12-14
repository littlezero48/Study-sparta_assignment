package com.example.assignment_memo.util.security;

import com.example.assignment_memo.entity.User;
import com.example.assignment_memo.entity.UserRoleEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class UserDetailsImpl implements UserDetails {

    private final User user;
    private final String username;

    // @RequiredArgsConstructor 역할 하는 생성자
    public UserDetailsImpl(User user, String username) {
        this.user = user;
        this.username = username;
    }

    // @Getter 역할하는 메소드
    public User getUser() { return user; }

    @Override
    public String getUsername(){ return this.username; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRoleEnum role = user.getRole();                                         // 유저의 role을 가져와 role Enum에 대입
        String authority = role.getAuthority();                                     // 그 role명에 맞는 문자열을 가져온다

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);  // 권한 객체 생성
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

    @Override
    public String getPassword(){ return null; }

    @Override
    public boolean isAccountNonExpired(){ return false; }

    @Override
    public boolean isAccountNonLocked(){ return false; }

    @Override
    public boolean isCredentialsNonExpired(){ return false; }

    @Override
    public boolean isEnabled(){ return false; }
}
