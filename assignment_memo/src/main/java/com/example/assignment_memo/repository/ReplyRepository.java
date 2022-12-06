package com.example.assignment_memo.repository;

import com.example.assignment_memo.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply,Long> {
    Optional<Reply> findByMemo_MemoIdAndReplyUid(Long id, Long replyId);
}
