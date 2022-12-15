package com.example.assignment_memo.repository;

import com.example.assignment_memo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// 메모 레파지토리
public interface MemoRepository extends JpaRepository<Memo, Long>, MemoRepositoryCustom {

    Optional<Memo> findById(Long Id);
    List<Memo> findAllByOrderByCreatedAtDesc();                                                                  // findAllBy(모두찾는다)/OrderBy(-를 기준으로 정렬로)/ModifiedAt(ModifiedAt멤버변수를)/Desc(내림차순)
    Long countLikeFromLikeMemo(Long Id);

}
