package com.example.assignment_memo.repository;

import com.example.assignment_memo.entity.QLikeMemo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

@Repository
public class MemoRepositoryImpl implements MemoRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory; // 의존성 주입

    public MemoRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Long countLikeFromLikeMemo(Long memoId){
        // Q클래스 사용
        QLikeMemo likeMemo = QLikeMemo.likeMemo;
        return jpaQueryFactory
                .select(likeMemo.count())
                .from(likeMemo)
                .where(
                        likeMemo.memo.id.eq(memoId)
                )
                .fetchOne();                              // 하나의 값만 반환
    }
}
