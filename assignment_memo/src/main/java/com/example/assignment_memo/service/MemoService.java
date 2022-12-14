package com.example.assignment_memo.service;

import com.example.assignment_memo.dto.*;
import com.example.assignment_memo.entity.Memo;
import com.example.assignment_memo.entity.Reply;
import com.example.assignment_memo.entity.User;
import com.example.assignment_memo.entity.UserRoleEnum;
import com.example.assignment_memo.repository.MemoRepository;
import com.example.assignment_memo.repository.ReplyRepository;
import com.example.assignment_memo.repository.UserRepository;
import com.example.assignment_memo.util.ApiResponse.CustomException;
import com.example.assignment_memo.util.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.assignment_memo.util.ApiResponse.CodeError.MEMO_NOT_FOUND;
import static com.example.assignment_memo.util.ApiResponse.CodeError.NO_ACCESS;

@Service                        // 이건 서비스다! 선언
@RequiredArgsConstructor        // 생성자 자동 주입
public class MemoService {

    private final MemoRepository memoRepository;        // 메모 레포지토리를 사용할 수 있게 객체 선언 // 서비스든 컨트롤이든 클래스 연결할때 final 선언안해주면 오류남
    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;
    private final JwtUtil jwtUtil;


    // 전체 글 조회
    public MessageDto getMemos(){
        List<Memo> memolist = memoRepository.findAllByOrderByCreatedAtDesc(); // 아 여기서 데이터 값을 가져오는 메소드를 커스텀 하려면 리포지토리에서 작성해야 한다.
        List<MemoResponseDto> responseDtoList = new ArrayList<>();

        for(Memo memo : memolist){

            MemoResponseDtoBuilder mrdBuilder = new MemoResponseDtoBuilder();
            MemoResponseDto responseDto =
                    mrdBuilder.id(memo.getMemoId())
                            .title(memo.getTitle())
                            .username(memo.getUsername())
                            .content(memo.getContent())
                            .createdAt(memo.getCreatedAt())
                            .modifiedAt(memo.getModifiedAt())
                            .addReply(memo.getReplies())            // Entity -> Dto로 전환
                            .getMemos();

            responseDtoList.add(responseDto);
        }
        return new MessageDto(StatusEnum.OK, responseDtoList);
    }

    // 선택 글 조회 기능
    public MessageDto getMemos(Long id){                            // 해당 글 하나만 읽기
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        MemoResponseDtoBuilder mrdBuilder = new MemoResponseDtoBuilder();
        MemoResponseDto responseDto =
                mrdBuilder.id(memo.getMemoId())
                        .title(memo.getTitle())
                        .username(memo.getUsername())
                        .content(memo.getContent())
                        .createdAt(memo.getCreatedAt())
                        .modifiedAt(memo.getModifiedAt())
                        .addReply(memo.getReplies())                // Entity -> Dto로 전환
                        .getMemos();

        return new MessageDto(StatusEnum.OK, responseDto);          // Entity -> Dto로 전환
    }

    // 글 작성 기능
    public MessageDto createMemo(MemoRequestDto dto, User user){

        Memo memo = new Memo(dto, user);        // 컨트롤러에서 @RequestBody 어노테이션으로 body의 내용을 가져온건데 또 할 필요 없겠지
        memoRepository.save(memo);                            // insert   // save자체에 Transactional을 생기게 하는 로직이 있다

        MemoResponseDtoBuilder mrdBuilder = new MemoResponseDtoBuilder();
        MemoResponseDto responseDto =
                mrdBuilder.id(memo.getMemoId())
                        .title(memo.getTitle())
                        .username(memo.getUsername())
                        .content(memo.getContent())
                        .createdAt(memo.getCreatedAt())
                        .modifiedAt(memo.getModifiedAt())
                        .getMemos();

        return new MessageDto(StatusEnum.OK, responseDto);                                      // 결과값을 다시 리턴
    }

    // 글 수정 기능
    @Transactional  // 트랜잭셔널은 DB의 값을 변화를 줄때 필요한데 다른 DB CRUD에는 이게 기본적으로 있는데 update만 없다고..
    public MessageDto modifyMemo (Long id, MemoRequestDto dto, User user) {
        //findById(id) :  id 기준으로 검색
        //orElseTrow() : 검색시 에러 발생시 예외를 던진다
        // () ->  : optional 인자가 null경우
        //new IllegalArgumentException("메세지") : 부적절한 인수, 부정한 인수를 메서드에 건네준 예외 임을 메세지와 함께 알린다.
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );


        if(accessPermission(memo.getUsername(), user.getUsername(), user.getRole())) {

            memo.update(dto);  // update는 entity에 새로 정의한 함수

            MemoResponseDtoBuilder mrdBuilder = new MemoResponseDtoBuilder();
            MemoResponseDto responseDto =
                    mrdBuilder.id(memo.getMemoId())
                            .title(memo.getTitle())
                            .username(memo.getUsername())
                            .content(memo.getContent())
                            .createdAt(memo.getCreatedAt())
                            .modifiedAt(memo.getModifiedAt())
                            .addReply(memo.getReplies())
                            .getMemos();

            return new MessageDto( StatusEnum.OK, responseDto);
        }
        throw new CustomException(NO_ACCESS);
    }


    // 글 삭제 기능
    @Transactional
    public MessageDto deleteMemo (Long id, User user) {
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 글이 존재하지 않습니다.")
        );

        if(accessPermission(memo.getUsername(), user.getUsername(), user.getRole())) {  // 유저 대조
            memoRepository.deleteById(id);                                              // delete자체에 Transactional을 생기게 하는 로직이 있다
            return new MessageDto(StatusEnum.OK);
        }
        throw new CustomException(NO_ACCESS);
    }

    // 댓글 작성 기능
    public MessageDto createReply(Long id, ReplyRequestDto dto, User user) {
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        Reply newOne = new Reply (dto, user, memo);               // 컨트롤러에서 @RequestBody 어노테이션으로 body의 내용을 가져온건데 또 할 필요 없겠지
        replyRepository.save(newOne);                                           // insert   // save자체에 Transactional을 생기게 하는 로직이 있다
        ReplyResponseDto responseDto = new ReplyResponseDto(newOne);            // Entity -> Dto로 전환
        return new MessageDto(StatusEnum.OK, responseDto);
    }

    // 댓글 수정 기능
    @Transactional
    public MessageDto modifyReply(Long id, Long replyId, ReplyRequestDto dto, User user) {
        Reply reply = replyRepository.findByMemo_MemoIdAndReplyId(id, replyId).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        if(accessPermission(reply.getReplyName(), user.getUsername(), user.getRole())) {
            reply.update(dto);
            ReplyResponseDto responseDto = new ReplyResponseDto(reply);                     // Entity -> Dto로 전환
            return new MessageDto(StatusEnum.OK, responseDto);
        }
        throw new CustomException(NO_ACCESS);
    }

    // 댓글 삭제 기능
    @Transactional
    public MessageDto deleteReply(Long id, Long replyId, User user) {      // 부모클래스인 MessageDto로 리턴타입을 정하고 UserDto도 사용해 다형성 사용
        Reply reply = replyRepository.findByMemo_MemoIdAndReplyId(id, replyId).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        if (accessPermission(reply.getReplyName(), user.getUsername(), user.getRole())) {
            replyRepository.deleteByReplyId(replyId);                            // insert   // save자체에 Transactional을 생기게 하는 로직이 있다
            return new MessageDto(StatusEnum.OK);
        }
        throw new CustomException(NO_ACCESS);
    }

    // 작성자 일치 여부 체크 및 ADMIN 허가 적용
    public boolean accessPermission (String nameInEntity, String nameInRequest, UserRoleEnum role ){
        if(nameInEntity.equals(nameInRequest) || role == UserRoleEnum.ADMIN){
            return true;
        } else {
            return false;
        }
    }
}

// keyword : 직렬화(Serialization) 와 역직렬화(Deserialization)
// 트랜잭션 - DB를 다루는 특정행동, DB가 변화하면 트랜잭션이 생김
