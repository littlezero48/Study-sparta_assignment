package com.example.assignment_memo.entity;

import com.example.assignment_memo.dto.ReplyRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Reply extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "MEMO_ID", nullable = false)
    private Memo memo;              //JPA에서는 필드 하나가 아닌 객체로 연결함

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String replyName;

    @Column(nullable = false)
    private String replyContent;

    public Reply(ReplyRequestDto dto, User user, Memo memo){
        this.memo = memo;
        this.replyName = user.getUsername();
        this.replyContent = dto.getReplyContent();
        this.user = user;
    }

    public void update(ReplyRequestDto dto){
        this.replyContent = dto.getReplyContent();
    }
}
