package com.example.assignment_memo.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class MemoResponseDto {

    private Long id;
    private String title;
    private String username;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private List<ReplyResponseDto> replies = new ArrayList<>();

//    public MemoResponseDto(Memo memo) {
//        this.id = memo.getMemoId();
//        this.title = memo.getTitle();
//        this.username = memo.getUsername();
//        this.content = memo.getContent();
//        this.createdAt = memo.getCreatedAt();
//        this.modifiedAt = memo.getModifiedAt();
//    }
//
//    public MemoResponseDto(Memo memo, List<ReplyResponseDto> replies) {
//        this.id = memo.getMemoId();
//        this.title = memo.getTitle();
//        this.username = memo.getUsername();
//        this.content = memo.getContent();
//        this.createdAt = memo.getCreatedAt();
//        this.modifiedAt = memo.getModifiedAt();
//        this.replies = replies;
//    }

        public MemoResponseDto(Long id, String title, String username, String content, LocalDateTime createdAt, LocalDateTime modifiedAt, List<ReplyResponseDto> replies) {
        this.id = id;
        this.title = title;
        this.username = username;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.replies = replies;
    }
}
