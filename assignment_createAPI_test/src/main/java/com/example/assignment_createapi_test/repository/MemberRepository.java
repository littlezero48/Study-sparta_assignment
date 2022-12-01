package com.example.assignment_createapi_test.repository;

import com.example.assignment_createapi_test.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member,Long> {
}
