package com.example.assignment_memo.entity;

import com.example.assignment_memo.dto.MemoRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter                                                                                                                 // Class 모든 필드의 Getter method를 생성
@Entity                                                                                                                 // Entity임을 선언
@NoArgsConstructor                                                                                                      // @NoArgsConstructor : 파라미터가 없는 기본 생성자를 생성
public class Memo extends Timestamped {

    @Id                                                                                                                 // ID임을 선언
    @Column(name = "MEMO_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)                                                                     // 값 자동 생성 , 생성 전략 : 자동 증감
    private Long memoId;                                                                                                // long의 래퍼클래스

    @Column(nullable = false)                                                                                           // 컬럼 설정 , null값 허용 선택 : 불가
    private String title;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")                                                                                       // 이 객체정보가 들어갈 테이블 이름
    private User user;

    @OneToMany(mappedBy = "memo", cascade = CascadeType.REMOVE)                                                         // 글 하나가 삭제되면 맵핑되어있는 쪽 객체이름, cascade 연속성 전이 속성
    @OrderBy("createdAt desc ")                                                                                         // 엔티티단에서 정렬
    private List<Reply> replies = new ArrayList<>();                                                                    // 일대다의 다 부분을 List로 받기



//    @JsonIgnore                                                                                                       // 이 어노테이션을 쓰면 화면으로 가는 DTO에 노출되지 않는다.
//    @Column(nullable = false)
//    private String password;

    public Memo(MemoRequestDto dto, User user){                    // 생성자

        this.title = dto.getTitle();
        this.username = user.getUsername();
        this.content = dto.getContent();
        this.user = user;

    }

    public void update(MemoRequestDto dto){             // 수정 메소드

        this.title = dto.getTitle();
        this.content = dto.getContent();
    }


}
