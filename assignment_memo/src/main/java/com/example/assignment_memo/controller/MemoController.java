package com.example.assignment_memo.controller;

import com.example.assignment_memo.dto.MemoRequestDto;
import com.example.assignment_memo.dto.MemoResponseDto;
import com.example.assignment_memo.dto.PublicDto;
import com.example.assignment_memo.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController             // 컨트롤러 선언
@RequiredArgsConstructor    // 생성자 자동 주입
public class MemoController {

    private final MemoService memoService;

    @GetMapping("/")
    public ModelAndView home(){ return new ModelAndView("index"); }               // 클래스 자체가 Model과 View를 같이 하는 클래스구나

    @GetMapping("/api/memos") // GET방식
    public List<MemoResponseDto> memoRequest (){                                            // @RequestBody 어노테이션으로 body의 내용을 가져옴
        return memoService.getMemos();
    }

    @PostMapping("/api/memoWrite")  //POST방식
    public MemoResponseDto memoWrite (@RequestBody MemoRequestDto dto) {
        return memoService.writeMemo(dto);
    }

    @PutMapping("/api/memoModify/{id}") // PUT방식
    public MemoResponseDto memoModify(@PathVariable Long id, @RequestBody MemoRequestDto dto) {
        return memoService.modifyMemo(id, dto);
    }

    @DeleteMapping("/api/memoDelete/{id}")  // DELETE방식
    public PublicDto memoDelete(@PathVariable Long id, @RequestBody MemoRequestDto dto) {
        return memoService.deleteMemo(id, dto.getPassword());
    }
}

// Request가 들어오는 타입 따라 밥는 방법 4가지
// 1. URL 변수          @PathVariable   구분자에 들어오는 값을 처리
// 2. Query String      @RequestParam   괄호안 전달 받을 인자값을 이름을 적고 그에 해당하는 값을 받아온다. 해당 인자값 이름이 없다면 BadRequest 4** 발생, 인자값이 많아지면 hashmap사용
// 3. Body              @RequstBody     값이 View 로 출력되지 않고 HTTP Response Body에 직접 쓰여진다. return 시에 json, xml과 같은 데이터를 return
// 4. Form