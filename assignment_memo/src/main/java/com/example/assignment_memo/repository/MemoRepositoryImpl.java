package com.example.assignment_memo.repository;

import com.example.assignment_memo.entity.QLikeMemo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemoRepositoryImpl implements MemoRepositoryCustom {

    private final JPAQueryFactory queryFactory; // 의존성 주입

    @Override
    public Long countLikeFromLikeMemo(Long replyId){
        // Q클래스 사용
        QLikeMemo likeMemo = QLikeMemo.likeMemo;
        return queryFactory
                .select(likeMemo.count())                   // 개수
                .from(likeMemo)
                .where(
                        likeMemo.id.eq(replyId)
                )
                .fetchFirst();                              // 하나의 값만 반환
    }
}
