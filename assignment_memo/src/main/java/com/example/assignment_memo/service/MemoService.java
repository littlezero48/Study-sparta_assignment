package com.example.assignment_memo.service;

import com.example.assignment_memo.dto.*;
import com.example.assignment_memo.entity.Memo;
import com.example.assignment_memo.entity.Reply;
import com.example.assignment_memo.entity.User;
import com.example.assignment_memo.entity.UserRoleEnum;
import com.example.assignment_memo.jwt.JwtUtil;
import com.example.assignment_memo.repository.MemoRepository;
import com.example.assignment_memo.repository.ReplyRepository;
import com.example.assignment_memo.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service                        // 이건 서비스다! 선언
@RequiredArgsConstructor        // 생성자 자동 주입
public class MemoService {

    private final MemoRepository memoRepository;                                // 메모 레포지토리를 사용할 수 있게 객체 선언 // 서비스든 컨트롤이든 클래스 연결할때 final 선언안해주면 오류남
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;

    // 전체 글 조회
    public List<MemoResponseDto> getMemos(){
        List<Memo> memolist = memoRepository.findAllByOrderByModifiedAtDesc(); // 아 여기서 데이터 값을 가져오는 메소드를 커스텀 하려면 리포지토리에서 작성해야 한다.
        List<MemoResponseDto> exportDtoList = new ArrayList<>();

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

            exportDtoList.add(responseDto);
        }
        return exportDtoList;
    }

    // 선택 글 조회 기능
    public MemoResponseDto getMemos(Long id){                       // 해당 글 하나만 읽기
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 글이 존재하지 않습니다.")
        );

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

        return responseDto;                         // Entity -> Dto로 전환
    }

    // 글 작성 기능
    public PublicDto writeMemo(MemoRequestDto dto, HttpServletRequest request){
        String token = jwtUtil.resolveToken(request);               // Request에서 Token 가져오기
        Claims claims;                                              // 사용자 정보 Claims을 가져올 변수
        PublicDto exportDto = new PublicDto();

        if(token != null){                                          //token없으면 글 생성 불가
            User user = validateUser(token);

            Memo memo = new Memo(dto, user.getUsername());        // 컨트롤러에서 @RequestBody 어노테이션으로 body의 내용을 가져온건데 또 할 필요 없겠지
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

            return responseDto;                                       // 결과값을 다시 리턴
        } else {
            exportDto.setResult(0, "Token이 없습니다" );
            return exportDto;
        }
    }

    // 글 수정 기능
    @Transactional  // 트랜잭셔널은 DB의 값을 변화를 줄때 필요한데 다른 DB CRUD에는 이게 기본적으로 있는데 update만 없다고..
    public PublicDto modifyMemo (Long id, MemoRequestDto dto, HttpServletRequest request) {
        //findById(id) :  id 기준으로 검색
        //orElseTrow() : 검색시 에러 발생시 예외를 던진다
        // () ->  : optional 인자가 null경우
        //new IllegalArgumentException("메세지") : 부적절한 인수, 부정한 인수를 메서드에 건네준 예외 임을 메세지와 함께 알린다.
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 글이 존재하지 않습니다.")
        );

        String token = jwtUtil.resolveToken(request);
        Claims claims;
        PublicDto exportDto = new PublicDto();

        if(token != null) {
            User user = validateUser(token);

            if (memo.getUsername().equals(user.getUsername()) || user.getRole() == UserRoleEnum.ADMIN) {
                memo.update(dto);  // update는 entity에 새로 정의한 함수

                MemoResponseDtoBuilder mrdBuilder = new MemoResponseDtoBuilder();
                MemoResponseDto responseDto =
                        mrdBuilder.id(memo.getMemoId())
                                .title(memo.getTitle())
                                .content(memo.getContent())
                                .modifiedAt(memo.getModifiedAt())
                                .getMemos();

                responseDto.setResult(200,"글 수정 성공입니다.");
                return responseDto;
            }
            return null;
        } else {
            exportDto.setResult(0,"token이 없습니다.");
            return exportDto;
        }
    }

    // 글 삭제 기능
    public PublicDto deleteMemo (Long id, HttpServletRequest request) {
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 글이 존재하지 않습니다.")
        );

        String token = jwtUtil.resolveToken(request);
        PublicDto exportDto = new PublicDto();

        if(token != null) {
            User user = validateUser(token);

            if (memo.getUsername().equals(user.getUsername()) || user.getRole() == UserRoleEnum.ADMIN) {           // 유저 대조
                memoRepository.deleteById(id);                                  // delete자체에 Transactional을 생기게 하는 로직이 있다
                exportDto.setResult(200,"글 삭제");
            }
            return exportDto;
        } else {
            exportDto.setResult(0,"token이 없습니다.");
            return exportDto;
        }
    }

    // 댓글 작성 기능
    public PublicDto createReply(Long id, ReplyRequestDto dto, HttpServletRequest request) {
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 글이 존재하지 않습니다.")
        );

        String token = jwtUtil.resolveToken(request);
        PublicDto exportDto;

        if(token != null){
            User user = validateUser(token);

            Reply newOne = new Reply (dto, user.getUsername(), memo);          // 컨트롤러에서 @RequestBody 어노테이션으로 body의 내용을 가져온건데 또 할 필요 없겠지
            replyRepository.save(newOne);                                           // insert   // save자체에 Transactional을 생기게 하는 로직이 있다
            exportDto = new ReplyResponseDto(newOne);                               // Entity -> Dto로 전환
            return exportDto;
        }
        return null;
    }

    // 댓글 수정 기능
    public PublicDto modifyReply(Long id, ReplyRequestDto dto, HttpServletRequest request) {
        Reply reply = replyRepository.findByMemo_MemoIdAndReplyUid(id, dto.getReplyId()).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
        );

        String token = jwtUtil.resolveToken(request);
        PublicDto exportDto;

        if(token != null){
            User user = validateUser(token);

            if(reply.getReplyName().equals(user.getUsername())){
                reply.update(dto);
                replyRepository.save(reply);                                           // insert   // save자체에 Transactional을 생기게 하는 로직이 있다
                exportDto = new ReplyResponseDto(reply);                               // Entity -> Dto로 전환
                return exportDto;
            }
        }
        return null;
    }

    // 유저 체크
    public User validateUser(String token){
        Claims claims;

        if (jwtUtil.validateToken(token)) {                 // token이 유효한 거면 생성 가능
            claims = jwtUtil.getUserInfoFromToken(token);   // 토큰에서 사용자 정보 가져오기
        } else {
            throw new IllegalArgumentException("Token Error");
        }

        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
        );

        return user;
    }
}

// keyword : 직렬화(Serialization) 와 역직렬화(Deserialization)
// 트랜잭션 - DB를 다루는 특정행동, DB가 변화하면 트랜잭션이 생김
