package com.example.assignment_memo.util.ApiResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
// 전역으로 사용할 CustomException클래스
public class CustomException extends RuntimeException {                                                                 // 실행 예외에 대한 클래스를 상속받아서 Unchecked Exception 으로 활용. 이걸 상속하니 아이콘에 번개생김
    private final CodeError codeError;

}
