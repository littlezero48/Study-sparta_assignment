package com.example.assignment_memo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing  // Audit 시간을 사용하려면 이 어노테이션을 꼭 사용해야한다
@SpringBootApplication
public class AssignmentMemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssignmentMemoApplication.class, args);
    }

}



// 트러블 슈팅
// final 선언 안해줘서 repository 오류
// postman 제대로된 값을 전달 못해서 405오류
// Audit 기능 사용하면서 실행 클래스에 어노테이션 추가 안해서 값이 null값으로 들어간거
// Dto 일부값을 숨기기는 @JsonIgnore 사용
