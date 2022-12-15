package com.example.assignment_memo.repository;

import com.example.assignment_memo.entity.LikeMemo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeMemoRepository extends JpaRepository<LikeMemo, Long> {
    Optional<LikeMemo> findAllById(Long id);
    void deleteById(Long id);
    Optional<LikeMemo> findByMemo_IdAndUser_Id(Long memoId, Long replyId);
}
