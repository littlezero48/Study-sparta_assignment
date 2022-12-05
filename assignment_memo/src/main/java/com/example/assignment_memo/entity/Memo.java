package com.example.assignment_memo.entity;

import com.example.assignment_memo.dto.MemoRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter // Class 모든 필드의 Getter method를 생성
@Entity // Entity임을 선언
@NoArgsConstructor // @NoArgsConstructor : 파라미터가 없는 기본 생성자를 생성
public class Memo extends Timestamped {

    @Id     // ID임을 선언
    @GeneratedValue(strategy = GenerationType.AUTO)     // 값 자동 생성 , 생성 전략 : 자동 증감
    private Long id;                                    // int값이 면 안되나?? 왜 repository에서 타입에 안되지

    @Column(nullable = false)                           // 컬럼 설정 , null값 허용 선택 : 불가
    private String title;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String content;

//    @JsonIgnore                                         // 이 어노테이션을 쓰면 화면으로 가는 DTO에 노출되지 않는다.
//    @Column(nullable = false)
//    private String password;


    // 타임스탬프는 extends 한 이후로 어떻게 쓰지? 자동으로 column 저장되나? 오 여기서 별도의 처리를 해주지 않아도 컬럼으로 추가된다.
    // 그런데 값이 안들어감. 실행클래스에서 @EnableJpaAuditing를 써야 시간 감시를 시작함 유효값이 들어가기 시작.

    public Memo(MemoRequestDto dto, String username){                    // 생성자

        this.title = dto.getTitle();
        this.username = username;
        this.content = dto.getContent();

    }

    public void update(MemoRequestDto dto){             // 수정 메소드

        this.title = dto.getTitle();
        this.content = dto.getContent();
    }


}
