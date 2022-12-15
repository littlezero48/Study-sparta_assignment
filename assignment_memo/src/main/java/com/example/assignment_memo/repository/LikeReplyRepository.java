package com.example.assignment_memo.repository;

import com.example.assignment_memo.entity.LikeReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeReplyRepository extends JpaRepository<LikeReply, Long> {
    Optional<LikeReply> findAllByUserId(Long id);
    void deleteByMemoId(Long id);
    // 메모 ID와 댓글 ID, 유저 ID 일치 조건 select
    Optional<LikeReply> findByMemo_IdAndReply_IdAndUser_Id(Long memoId, Long ReplyId, Long UserId);
}
