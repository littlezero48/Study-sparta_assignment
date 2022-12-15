package com.example.assignment_memo.service;

import com.example.assignment_memo.dto.*;
import com.example.assignment_memo.entity.*;
import com.example.assignment_memo.repository.*;
import com.example.assignment_memo.util.ApiResponse.CodeSuccess;
import com.example.assignment_memo.util.ApiResponse.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.assignment_memo.util.ApiResponse.CodeError.*;

@Service                                                                                                                // Component 포함 / Bean등록
@RequiredArgsConstructor                                                                                                // final등 필수적인 필드를 초기화하는 생성자 자동 주입
public class MemoService {

    private final MemoRepository memoRepository;                                                                        // 메모 레포지토리를 사용할 수 있게 의존성 주입 // 서비스든 컨트롤이든 클래스 연결할때 final 선언안해주면 오류남
    private final ReplyRepository replyRepository;
    private final LikeMemoRepository likeMemoRepository;
    private final LikeReplyRepository likeReplyRepository;
    private final MemoRepositoryImpl memoRepositoryImpl;
    private final ReplyRepositoryImpl replyRepositoryImpl;


    // 전체 글 조회
    public MessageDto<?> getMemos(){
        // 1. 전체 글을 생성한 날짜로 모두 조회
        List<Memo> memolist = memoRepository.findAllByOrderByCreatedAtDesc();                                           // 아 여기서 데이터 값을 가져오는 메소드를 커스텀 하려면 리포지토리에서 작성해야 한다.
        List<MemoResponseDto> responseDtoList = new ArrayList<>();

        // 2. 글 하나씩 MemoResponseDto로 전환
        for(Memo memo : memolist){
            MemoResponseDtoBuilder mrdBuilder = new MemoResponseDtoBuilder();
            MemoResponseDto responseDto =
                    mrdBuilder.id(memo.getId())
                            .title(memo.getTitle())
                            .username(memo.getUsername())
                            .content(memo.getContent())
                            .likeCnt(memoRepositoryImpl.countLikeFromLikeMemo(memo.getId()))
                            .createdAt(memo.getCreatedAt())
                            .modifiedAt(memo.getModifiedAt())
                            // 3. 댓글 하나씩 ReplyResponseDto로 전환해 매개값으로 받음
                            .addReply(addLikeCntToReplyResponseDto(memo.getReplies()))
                            .getMemos();

            responseDtoList.add(responseDto);
        }
        // 4. 성공 메세지와 반환 데이터를 MessageDto<?>(Controller행)로 반환
        return new MessageDto<>(CodeSuccess.GET_OK, responseDtoList);
    }

    // 선택 글 조회 기능
    public MessageDto<?> getMemos(Long id){
        // 1. 메모의 존재 여부 확인
        Memo memo = memoRepository.findById(id).orElseThrow(                                                        //findById(id) :  id 기준으로 검색 // orElseTrow() : 검색시 에러 발생시 예외를 던진다 // () ->  : optional 인자가 null경우
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        // 2. 해당 메모를 MemoResponseDto로 전환
        MemoResponseDtoBuilder mrdBuilder = new MemoResponseDtoBuilder();
        MemoResponseDto responseDto =
                mrdBuilder.id(memo.getId())
                        .title(memo.getTitle())
                        .username(memo.getUsername())
                        .content(memo.getContent())
                        .likeCnt(memoRepositoryImpl.countLikeFromLikeMemo(memo.getId()))
                        .createdAt(memo.getCreatedAt())
                        .modifiedAt(memo.getModifiedAt())
                        // 3. 댓글 하나씩 ReplyResponseDto로 전환해 매개값으로 받음
                        .addReply(addLikeCntToReplyResponseDto(memo.getReplies()))
                        .getMemos();

        // 4. 성공 메세지와 반환 데이터를 MessageDto<?>(Controller행)로 반환
        return new MessageDto<>(CodeSuccess.GET_OK, responseDto);
    }

    // 글 작성 기능
    public MessageDto<?> createMemo(MemoRequestDto dto, User user){
        // 1. 메모 저장
        Memo memo = new Memo(dto, user);                                                                                // 컨트롤러에서 @RequestBody 어노테이션으로 body의 내용을 가져온건데 또 할 필요 없겠지
        memoRepository.save(memo);                                                                                      // insert   // save자체에 Transactional을 생기게 하는 로직이 있다

        // 2. 해당 메모를 MemoResponseDto로 전환
        MemoResponseDtoBuilder mrdBuilder = new MemoResponseDtoBuilder();
        MemoResponseDto responseDto =
                mrdBuilder.id(memo.getId())
                        .title(memo.getTitle())
                        .username(memo.getUsername())
                        .content(memo.getContent())
                        .createdAt(memo.getCreatedAt())
                        .modifiedAt(memo.getModifiedAt())
                        .getMemos();

        // 3. 성공 메세지와 반환 데이터를 MessageDto<?>(Controller행)로 반환
        return new MessageDto<>(CodeSuccess.CREATE_OK, responseDto);
    }

    // 글 수정 기능
    @Transactional                                                                                                      // 트랜잭셔널은 DB의 값을 변화를 줄때 필요 // 트랜잭션 - DB를 다루는 특정행동, DB가 변화하면 트랜잭션이 생김
    public MessageDto<?> modifyMemo (Long id, MemoRequestDto dto, User user) {
        // 1. 메모의 존재 여부 확인
        Memo memo = memoRepository.findById(id).orElseThrow(                                                            //findById(id) :  id 기준으로 검색 // orElseTrow() : 검색시 에러 발생시 예외를 던진다 // () ->  : optional 인자가 null경우
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        // 2. 권한 체크 (작성자, ADMIN만 허가)
        if (memo.getUsername().equals(user.getUsername()) || user.getRole() == UserRoleEnum.ADMIN) {
            // 3. 메모를 수정
            memo.update(dto);

            // 4. 해당 메모를 MemoResponseDto로 전환
            MemoResponseDtoBuilder mrdBuilder = new MemoResponseDtoBuilder();
            MemoResponseDto responseDto =
                    mrdBuilder.id(memo.getId())
                            .title(memo.getTitle())
                            .username(memo.getUsername())
                            .content(memo.getContent())
                            .likeCnt(memoRepositoryImpl.countLikeFromLikeMemo(memo.getId()))
                            .createdAt(memo.getCreatedAt())
                            .modifiedAt(memo.getModifiedAt())
                            .addReply(addLikeCntToReplyResponseDto(memo.getReplies()))
                            .getMemos();

            // 5. 성공 메세지와 반환 데이터를 MessageDto<?>(Controller행)로 반환
            return new MessageDto<>(CodeSuccess.MODIFY_OK, responseDto);
        }
        throw new CustomException(NO_ACCESS);
    }


    // 글 삭제 기능
    @Transactional
    public MessageDto<?> deleteMemo (Long id, User user) {
        // 1. 메모의 존재 여부 확인
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 글이 존재하지 않습니다.")
        );

        // 2. 권한 체크 (작성자, ADMIN만 허가)
        if (memo.getUsername().equals(user.getUsername()) || user.getRole() == UserRoleEnum.ADMIN) {

            // 3. 글 삭제
            memoRepository.deleteById(id);                                                                              // delete자체에 Transactional을 생기게 하는 로직이 있다

            // 4. 성공 메세지를 MessageDto<?>(Controller행)로 반환
            return new MessageDto<>(CodeSuccess.DELETE_OK);
        }
        throw new CustomException(NO_ACCESS);
    }

    // 댓글 작성 기능
    public MessageDto<?> createReply(Long id, ReplyRequestDto dto, User user) {
        // 1. 메모의 존재 여부 확인
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        // 2. 댓글 저장
        Reply newOne = new Reply (dto, user, memo);
        replyRepository.save(newOne);                                                                                   // save자체에 Transactional을 생기게 하는 로직이 있다

        // 3. 해당 댓글을 ReplyResponseDto로 전환
        ReplyResponseDto responseDto = new ReplyResponseDto(newOne);

        // 4. 성공 메세지와 반환 데이터를 MessageDto<?>(Controller행)로 반환
        return new MessageDto<>(CodeSuccess.CREATE_OK, responseDto);
    }

    // 댓글 수정 기능
    @Transactional
    public MessageDto<?> modifyReply(Long id, Long replyId, ReplyRequestDto dto, User user) {
        // 1. 메모의 존재 여부 확인
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );
        // 2. 댓글의 존재 여부 확인
        Reply reply = replyRepository.findByMemo_IdAndId(id, replyId).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        // 3. 권한 체크 (작성자, ADMIN만 허가)
        if (reply.getUser().getId().equals(user.getId()) || user.getRole() == UserRoleEnum.ADMIN) {

            // 4. 댓글 수정
            reply.update(dto);
            ReplyResponseDto responseDto = new ReplyResponseDto(reply);

            // 5. 성공 메세지와 반환 데이터를 MessageDto<?>(Controller행)로 반환
            return new MessageDto<>(CodeSuccess.MODIFY_OK, responseDto);
        }
        throw new CustomException(NO_ACCESS);
    }

    // 댓글 삭제 기능
    @Transactional
    public MessageDto<?> deleteReply(Long id, Long replyId, User user) {                                                // 부모클래스인 MessageDto<?>로 리턴타입을 정하고 UserDto도 사용해 다형성 사용
        // 1. 메모의 존재 여부 확인
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );
        // 2. 댓글의 존재 여부 확인
        Reply reply = replyRepository.findByMemo_IdAndId(id, replyId).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        // 3. 권한 체크 (작성자, ADMIN만 허가)
        if (reply.getUser().getId().equals(user.getId()) || user.getRole() == UserRoleEnum.ADMIN) {
            // 4. 댓글 삭제
            replyRepository.deleteById(replyId);

            // 5. 성공 메세지를 MessageDto<?>(Controller행)로 반환
            return new MessageDto<>(CodeSuccess.DELETE_OK);
        }
        throw new CustomException(NO_ACCESS);
    }

    // 글 좋아요 추가
    public MessageDto hitMemoLike(Long id, User user){
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        Optional<LikeMemo> likes = likeMemoRepository.findByMemo_IdAndUser_Id(memo.getId(),user.getId());
        if(likes.isPresent()){
            throw new CustomException(DUPLICATE_RESOURCE);
        }

        LikeMemo like = new LikeMemo(user, memo);
        likeMemoRepository.save(like);

        MemoResponseDtoBuilder mrdBuilder = new MemoResponseDtoBuilder();
        MemoResponseDto responseDto =
                mrdBuilder.id(memo.getId())
                        .title(memo.getTitle())
                        .username(memo.getUsername())
                        .content(memo.getContent())
                        .likeCnt(memoRepositoryImpl.countLikeFromLikeMemo(memo.getId()))
                        .createdAt(memo.getCreatedAt())
                        .modifiedAt(memo.getModifiedAt())
                        .addReply(addLikeCntToReplyResponseDto(memo.getReplies()))
                        .getMemos();

        return new MessageDto<>(CodeSuccess.CREATE_OK, responseDto);
    }

    // 글 좋아요 취소
    public MessageDto cancelMemoLike(Long id, User user){
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        Optional<LikeMemo> likes = likeMemoRepository.findByMemo_IdAndUser_Id(memo.getId(),user.getId());
        if(likes.isEmpty()){
            throw new CustomException(NOT_FOUND);
        }

        likeMemoRepository.deleteById(likes.get().getId());
        return new MessageDto<>(CodeSuccess.DELETE_OK);
    }

    // 댓글 좋아요 추가
    public MessageDto hitReplyLike(Long id, Long replyId, User user){
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        Reply reply = replyRepository.findByMemo_IdAndId(id, replyId).orElseThrow(
                () -> new CustomException(REPLY_NOT_FOUND)
        );

        Optional<LikeReply> likes = likeReplyRepository.findByMemo_IdAndReply_IdAndUser_Id(memo.getId(),reply.getId(),user.getId());
        if(likes.isPresent()){
            throw new CustomException(DUPLICATE_RESOURCE);
        }

        LikeReply like = new LikeReply(user, memo, reply);
        likeReplyRepository.save(like);

        ReplyResponseDto responseDto = new ReplyResponseDto(reply, replyRepositoryImpl.countLikeFromLikeReply(replyId));
        return new MessageDto<>(CodeSuccess.CREATE_OK, responseDto);
    }

    // 댓글 좋아요 취소
    public MessageDto cancelReplyLike(Long id, Long replyId, User user){
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        Reply reply = replyRepository.findByMemo_IdAndId(id, replyId).orElseThrow(
                () -> new CustomException(REPLY_NOT_FOUND)
        );

        Optional<LikeReply> like = likeReplyRepository.findByMemo_IdAndReply_IdAndUser_Id(memo.getId(),reply.getId(),user.getId());
        if(like.isEmpty()){
            throw new CustomException(NOT_FOUND);
        }

        likeReplyRepository.deleteById(like.get().getId());

        return new MessageDto<>(CodeSuccess.DELETE_OK);
    }


    // 각 댓글마다 좋아요수 추가
    public List<ReplyResponseDto> addLikeCntToReplyResponseDto(List<Reply> replies){
        List<ReplyResponseDto> exportReplies = new ArrayList<>();
        for(int i=0; i<replies.size(); i++){
            Long likeCnt = replyRepositoryImpl.countLikeFromLikeReply(replies.get(i).getId());
            exportReplies.add(new ReplyResponseDto(replies.get(i), likeCnt));
        }

        return exportReplies;
    }
}

