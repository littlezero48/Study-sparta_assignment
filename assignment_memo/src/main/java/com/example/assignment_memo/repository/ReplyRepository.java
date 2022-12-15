package com.example.assignment_memo.repository;

import com.example.assignment_memo.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 댓글 레파지토리
public interface ReplyRepository extends JpaRepository<Reply,Long>, ReplyRepositoryCustom {
    Optional<Reply> findByMemo_IdAndId(Long id, Long replyId);
    void deleteById(Long replyId);
}
