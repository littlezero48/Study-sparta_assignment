package com.example.assignment_memo.service;

import com.example.assignment_memo.dto.MemoRequestDto;
import com.example.assignment_memo.dto.PublicDto;
import com.example.assignment_memo.entity.Memo;
import com.example.assignment_memo.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service    // 이건 서비스다! 선언
@RequiredArgsConstructor    // 생성자 자동 주입
public class MemoService {

    private final MemoRepository memoRepository;      // 메모 레포지토리를 사용할 수 있게 객체 선언 // 서비스든 컨트롤이든 클래스 연결할때 final 선언안해주면 오류남

    @Transactional(readOnly = true) // 읽기에 특화된 옵션?
    public List<Memo> getMemos(){
        return memoRepository.findAllByOrderByModifiedAtDesc(); // 아 여기서 데이터 값을 가져오는 메소드를 커스텀 하려면 리포지토리에서 작성해야 한다.
    }

    @Transactional
    public MemoRequestDto writeMemo(MemoRequestDto dto){
        Memo newOne = new Memo(dto);    // 컨트롤러에서 @RequestBody 어노테이션으로 body의 내용을 가져온건데 또 할 필요 없겠지
        memoRepository.save(newOne);    // insert
        return readyExportDto(newOne);                  // 결과값을 다시 리턴
    }

    @Transactional
    public MemoRequestDto modifyMemo (Long id, MemoRequestDto dto) {
        //findById(id) :  id 기준으로 검색
        //orElseTrow() : 검색시 에러 발생시 예외를 던진다
            // () ->  : optional 인자가 null경우
            //new IllegalArgumentException("메세지") : 부적절한 인수, 부정한 인수를 메서드에 건네준 예외 임을 메세지와 함께 알린다.
        Memo updateOne = memoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 글이 존재하지 않습니다.")
        );
        if(updateOne.getPassword().equals(dto.getPassword())){  // DB에서 가져온 패스워드랑 클라이언트에서 들고온 패스워드를 비교
            updateOne.update(dto);                              // update는 entity에 새로 정의한 함수
            return readyExportDto(updateOne);
        } else {
            // 수정이 안된걸 어떻게 처리해야하나
            Memo nullMemo = new Memo();
            return readyExportDto(updateOne);
        }
    }

    @Transactional
    public PublicDto deleteMemo (Long id, String pw) {
        Memo updateOne = memoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 글이 존재하지 않습니다.")
        );

        PublicDto result = new PublicDto();     // 메세지 처리를 위한 dto 객체 생성
        if (updateOne.getPassword().equals(pw)) {   // 비밀번호 대조
            memoRepository.deleteById(id);
            result.setResult("Success","글을 성공적으로 지웠습니다");
            return result;
        } else {
            result.setResult("Failed","글을 지우는데 실패했습니다");
            return result;
        }
    }

    public MemoRequestDto readyExportDto(Memo dto){
        MemoRequestDto exportDto = new MemoRequestDto();
        if(!dto.getId().equals(null)){
            exportDto.setId(dto.getId());
            exportDto.setContent(dto.getContent());
            exportDto.setTitle(dto.getTitle());
            exportDto.setAuthor(dto.getAuthor());
            exportDto.setCreatedAt(dto.getCreatedAt());
            exportDto.setModifiedAt(dto.getModifiedAt());
            exportDto.setResult("Success");
            exportDto.setMessage("성공했습니다.");
        } else {
            exportDto.setResult("Failed");
            exportDto.setMessage("실패했습니다.");
        }


        return exportDto;
    }

}
