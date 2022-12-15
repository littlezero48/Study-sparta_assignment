package com.example.assignment_memo.repository;

import com.example.assignment_memo.entity.QLikeReply;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class ReplyRepositoryImpl implements ReplyRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    public ReplyRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Long countLikeFromLikeReply(Long replyId) {
        QLikeReply likeReply = QLikeReply.likeReply;
        return jpaQueryFactory
                .select(likeReply.count())
                .from(likeReply)
                .where(
                        likeReply.id.eq(replyId)
                )
                .fetchFirst();
    }
}
