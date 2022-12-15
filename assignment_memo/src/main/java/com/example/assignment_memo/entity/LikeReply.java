package com.example.assignment_memo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity(name = "LikeReply")
@NoArgsConstructor
public class LikeReply {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;                                                                                                // 좋아요 Id

    @ManyToOne                                                                                                          // LikeReply(many) <-> User(one)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne                                                                                                          // LikeReply(many) <-> Memo(one)
    @JoinColumn(name = "MEMO_ID", nullable = false)
    private Memo memo;

    @ManyToOne                                                                                                          // LikeReply(many) <-> Reply(one)
    @JoinColumn(name = "REPLY_ID", nullable = false)
    private Reply reply;

    public LikeReply(User user, Memo memo, Reply reply) {
        this.user = user;
        this.memo = memo;
        this.reply = reply;
    }
}
