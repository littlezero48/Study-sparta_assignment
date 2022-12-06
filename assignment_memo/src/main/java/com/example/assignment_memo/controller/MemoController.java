package com.example.assignment_memo.controller;

import com.example.assignment_memo.dto.MemoRequestDto;
import com.example.assignment_memo.dto.MemoResponseDto;
import com.example.assignment_memo.dto.PublicDto;
import com.example.assignment_memo.dto.ReplyRequestDto;
import com.example.assignment_memo.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController             // 컨트롤러 선언
@RequiredArgsConstructor    // final 변수, Notnull 표시가 된 변수처럼 필수적인 정보를 세팅하는 생성자를 만든다.
public class MemoController {

    private final MemoService memoService;

//    @GetMapping("/")
//    public ModelAndView home(){ return new ModelAndView("index"); }               // 클래스 자체가 Model과 View를 같이 하는 클래스구나

    // 글 기능

    @GetMapping("/api/memos") // GET방식
    public List<MemoResponseDto> getMemos (){                                    // @RequestBody 어노테이션으로 body의 내용을 가져옴
        return memoService.getMemos();
    }

    @GetMapping("/api/memos/{id}") // GET방식                                    // 해당 글 하나만 읽기
    public MemoResponseDto getMemo (@PathVariable Long id){
        return memoService.getMemos(id);
    }

    @PostMapping("/api/memos")  //POST방식
    public PublicDto createMemo (@RequestBody MemoRequestDto dto, HttpServletRequest request) {
        return memoService.writeMemo(dto, request);
    }

    @PutMapping("/api/memos/{id}") // PUT방식
    public PublicDto modifyMemo(@PathVariable Long id, @RequestBody MemoRequestDto dto, HttpServletRequest request) {
        return memoService.modifyMemo(id, dto, request);
    }

    @DeleteMapping("/api/memos/{id}")  // DELETE방식
    public PublicDto deleteMemo(@PathVariable Long id, HttpServletRequest request) {
        return memoService.deleteMemo(id, request);
    }


    // 댓글 기능

    @PostMapping("/api/memos/{id}")
    public PublicDto createReply(@PathVariable Long id, @RequestBody ReplyRequestDto dto, HttpServletRequest request){
        return memoService.createReply(id, dto, request);
    }
}

// Request가 들어오는 타입 따라 밥는 방법 4가지
// 1. URL 변수 parameter    @PathVariable   구분자에 들어오는 값을 처리
// 2. Query String          @RequestParam   괄호안 전달 받을 인자값을 이름을 적고 그에 해당하는 값을 받아온다. 해당 인자값 이름이 없다면 BadRequest 4** 발생, 인자값이 많아지면 hashmap사용
// 3. Body                  @RequstBody     값이 View 로 출력되지 않고 HTTP Response Body에 직접 쓰여진다. return 시에 json, xml과 같은 데이터를 return