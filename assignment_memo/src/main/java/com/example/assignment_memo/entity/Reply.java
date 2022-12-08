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
    private Long replyId;


    @ManyToOne
//    @MapsId ("id")//반대쪽 @id로 선언한 필드를 외래키로 연결하고자 할때 // 이거 땜에 에러가 난다 - 500
    @JoinColumn(name = "MEMO_ID", nullable = false)
    private Memo memo; //객체로 연결해야해 필드 하나가 아니라

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
