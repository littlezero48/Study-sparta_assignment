package com.example.assignment_memo.service;

import com.example.assignment_memo.dto.*;
import com.example.assignment_memo.entity.Memo;
import com.example.assignment_memo.entity.Reply;
import com.example.assignment_memo.entity.User;
import com.example.assignment_memo.entity.UserRoleEnum;
import com.example.assignment_memo.repository.MemoRepository;
import com.example.assignment_memo.repository.ReplyRepository;
import com.example.assignment_memo.util.ApiResponse.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.assignment_memo.util.ApiResponse.CodeError.MEMO_NOT_FOUND;
import static com.example.assignment_memo.util.ApiResponse.CodeError.NO_ACCESS;

@Service                                                                                                                // Component 포함 / Bean등록
@RequiredArgsConstructor                                                                                                // final등 필수적인 필드를 초기화하는 생성자 자동 주입
public class MemoService {

    private final MemoRepository memoRepository;                                                                        // 메모 레포지토리를 사용할 수 있게 의존성 주입 // 서비스든 컨트롤이든 클래스 연결할때 final 선언안해주면 오류남
    private final ReplyRepository replyRepository;


    // 전체 글 조회
    public MessageDto getMemos(){
        // 1. 전체 글을 생성한 날짜로 모두 조회
        List<Memo> memolist = memoRepository.findAllByOrderByCreatedAtDesc();                                           // 아 여기서 데이터 값을 가져오는 메소드를 커스텀 하려면 리포지토리에서 작성해야 한다.
        List<MemoResponseDto> responseDtoList = new ArrayList<>();

        // 2. 글 하나씩 MemoResponseDto로 전환
        for(Memo memo : memolist){
            MemoResponseDtoBuilder mrdBuilder = new MemoResponseDtoBuilder();
            MemoResponseDto responseDto =
                    mrdBuilder.id(memo.getMemoId())
                            .title(memo.getTitle())
                            .username(memo.getUsername())
                            .content(memo.getContent())
                            .createdAt(memo.getCreatedAt())
                            .modifiedAt(memo.getModifiedAt())
                            // 3. 댓글 하나씩 ReplyResponseDto로 전환해 매개값으로 받음
                            .addReply(memo.getReplies())
                            .getMemos();

            responseDtoList.add(responseDto);
        }
        // 4. 성공 메세지와 반환 데이터를 MessageDto(Controller행)로 반환
        return new MessageDto(StatusEnum.OK, responseDtoList);
    }

    // 선택 글 조회 기능
    public MessageDto getMemos(Long id){
        // 1. 메모의 존재 여부 확인
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        // 2. 해당 메모를 MemoResponseDto로 전환
        MemoResponseDtoBuilder mrdBuilder = new MemoResponseDtoBuilder();
        MemoResponseDto responseDto =
                mrdBuilder.id(memo.getMemoId())
                        .title(memo.getTitle())
                        .username(memo.getUsername())
                        .content(memo.getContent())
                        .createdAt(memo.getCreatedAt())
                        .modifiedAt(memo.getModifiedAt())
                        // 3. 댓글 하나씩 ReplyResponseDto로 전환해 매개값으로 받음
                        .addReply(memo.getReplies())
                        .getMemos();

        // 4. 성공 메세지와 반환 데이터를 MessageDto(Controller행)로 반환
        return new MessageDto(StatusEnum.OK, responseDto);
    }

    // 글 작성 기능
    public MessageDto createMemo(MemoRequestDto dto, User user){
        // 1. 메모 저장
        Memo memo = new Memo(dto, user);                                                                                // 컨트롤러에서 @RequestBody 어노테이션으로 body의 내용을 가져온건데 또 할 필요 없겠지
        memoRepository.save(memo);                                                                                      // insert   // save자체에 Transactional을 생기게 하는 로직이 있다

        // 2. 해당 메모를 MemoResponseDto로 전환
        MemoResponseDtoBuilder mrdBuilder = new MemoResponseDtoBuilder();
        MemoResponseDto responseDto =
                mrdBuilder.id(memo.getMemoId())
                        .title(memo.getTitle())
                        .username(memo.getUsername())
                        .content(memo.getContent())
                        .createdAt(memo.getCreatedAt())
                        .modifiedAt(memo.getModifiedAt())
                        .getMemos();

        // 3. 성공 메세지와 반환 데이터를 MessageDto(Controller행)로 반환
        return new MessageDto(StatusEnum.OK, responseDto);
    }

    // 글 수정 기능
    @Transactional                                                                                                      // 트랜잭셔널은 DB의 값을 변화를 줄때 필요 // 트랜잭션 - DB를 다루는 특정행동, DB가 변화하면 트랜잭션이 생김
    public MessageDto modifyMemo (Long id, MemoRequestDto dto, User user) {
        // 1. 메모의 존재 여부 확인
        Memo memo = memoRepository.findById(id).orElseThrow(                                                            //findById(id) :  id 기준으로 검색 // orElseTrow() : 검색시 에러 발생시 예외를 던진다 // () ->  : optional 인자가 null경우
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        // 2. 권한 체크 (작성자, ADMIN만 허가)
        if(accessPermission(memo.getUsername(), user.getUsername(), user.getRole())) {
            // 3. 메모를 수정
            memo.update(dto);

            // 4. 해당 메모를 MemoResponseDto로 전환
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

            // 5. 성공 메세지와 반환 데이터를 MessageDto(Controller행)로 반환
            return new MessageDto( StatusEnum.OK, responseDto);
        }
        throw new CustomException(NO_ACCESS);
    }


    // 글 삭제 기능
    @Transactional
    public MessageDto deleteMemo (Long id, User user) {
        // 1. 메모의 존재 여부 확인
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 글이 존재하지 않습니다.")
        );

        // 2. 권한 체크 (작성자, ADMIN만 허가)
        if(accessPermission(memo.getUsername(), user.getUsername(), user.getRole())) {

            // 3. 글 삭제
            memoRepository.deleteById(id);                                                                              // delete자체에 Transactional을 생기게 하는 로직이 있다

            // 4. 성공 메세지를 MessageDto(Controller행)로 반환
            return new MessageDto(StatusEnum.OK);
        }
        throw new CustomException(NO_ACCESS);
    }

    // 댓글 작성 기능
    public MessageDto createReply(Long id, ReplyRequestDto dto, User user) {
        // 1. 메모의 존재 여부 확인
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        // 2. 댓글 저장
        Reply newOne = new Reply (dto, user, memo);
        replyRepository.save(newOne);                                                                                   // save자체에 Transactional을 생기게 하는 로직이 있다

        // 3. 해당 댓글을 ReplyResponseDto로 전환
        ReplyResponseDto responseDto = new ReplyResponseDto(newOne);

        // 4. 성공 메세지와 반환 데이터를 MessageDto(Controller행)로 반환
        return new MessageDto(StatusEnum.OK, responseDto);
    }

    // 댓글 수정 기능
    @Transactional
    public MessageDto modifyReply(Long id, Long replyId, ReplyRequestDto dto, User user) {
        // 1. 메모의 존재 여부 확인
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );
        // 2. 댓글의 존재 여부 확인
        Reply reply = replyRepository.findByMemo_MemoIdAndReplyId(id, replyId).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        // 3. 권한 체크 (작성자, ADMIN만 허가)
        if(accessPermission(reply.getReplyName(), user.getUsername(), user.getRole())) {

            // 4. 댓글 수정
            reply.update(dto);
            ReplyResponseDto responseDto = new ReplyResponseDto(reply);

            // 5. 성공 메세지와 반환 데이터를 MessageDto(Controller행)로 반환
            return new MessageDto(StatusEnum.OK, responseDto);
        }
        throw new CustomException(NO_ACCESS);
    }

    // 댓글 삭제 기능
    @Transactional
    public MessageDto deleteReply(Long id, Long replyId, User user) {                                                   // 부모클래스인 MessageDto로 리턴타입을 정하고 UserDto도 사용해 다형성 사용
        // 1. 메모의 존재 여부 확인
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );
        // 2. 댓글의 존재 여부 확인
        Reply reply = replyRepository.findByMemo_MemoIdAndReplyId(id, replyId).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        // 3. 권한 체크 (작성자, ADMIN만 허가)
        if (accessPermission(reply.getReplyName(), user.getUsername(), user.getRole())) {
            // 4. 댓글 삭제
            replyRepository.deleteByReplyId(replyId);

            // 5. 성공 메세지를 MessageDto(Controller행)로 반환
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

