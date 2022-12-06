package com.example.assignment_memo.dto;

import com.example.assignment_memo.entity.Reply;
import lombok.Getter;

@Getter
public class ReplyResponseDto extends PublicDto {

    private Long reply_id;
    private Long memo_uid;
    private String reply_name;
    private String reply_content;

    public ReplyResponseDto(Reply reply) {
        this.reply_id = reply.getReply_uid();
        this.memo_uid = reply.getMemo().getMemoId();
        this.reply_name = reply.getReply_name();
        this.reply_content = reply.getReply_content();
    }
}
