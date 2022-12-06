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
    private Long reply_uid;


    @ManyToOne
//    @MapsId ("id")//반대쪽 @id로 선언한 필드를 외래키로 연결하고자 할때 // 이거 땜에 에러가 난다 - 500
    @JoinColumn(name = "MEMO_ID", nullable = false)
    private Memo memo; //객체로 연결해야해 필드 하나가 아니라

    @Column(nullable = false)
    private String reply_name;

    @Column(nullable = false)
    private String reply_content;

    public Reply(ReplyRequestDto dto, String username, Memo memo){
        this.memo = memo;
        this.reply_name = username;
        this.reply_content = dto.getReply_content();
    }
}