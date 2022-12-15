package com.example.assignment_memo.repository;


import com.example.assignment_memo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 유저정보 레파지토리
public interface UserRepository extends JpaRepository<User, Long> {                                                     // <User, Long> 제네릭 멀티 타입


    Optional<User> findByUsername(String username);                                                                     // Optional은 객체를 포장해주는 래퍼클래스로 null값도 받을 수 있음
    Optional<User> findByUsernameAndPassword(String username, String password);
}

