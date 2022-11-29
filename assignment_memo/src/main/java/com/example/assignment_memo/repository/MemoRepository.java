package com.example.assignment_memo.repository;

import com.example.assignment_memo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    public List<Memo> findAllByOrderByModifiedAtDesc(); // findAllBy(모두찾는다)/OrderBy(-를 기준으로 정렬로)/ModifiedAt(ModifiedAt멤버변수를)/Desc(내림차순)

    @Query(value = "select m from memo m where m.id = :id, m.password = :pw", nativeQuery = true) // m은 테이블 별칭
    public Memo findById(Long id, String pw);
}
