package com.example.demo3.comment;

import com.example.demo3._core.errors.Exception403;
import com.example.demo3.board.Board;
import com.example.demo3.board.BoardRepository;
import com.example.demo3.user.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    // 댓글 작성
    @Transactional
    public void comment(CommentRequest.SaveDTO saveDTO, UserResponse.SessionDTO sessionUser) {
        log.info("댓글 작성 시작");

        // 게시글 조회
        Board board = boardRepository.findById(saveDTO.getBoardId())
                .orElseThrow(() ->
                        new RuntimeException("게시글을 찾을 수 없습니다"));

        Comment comment = Comment.builder()
                .content(saveDTO.getContent())
                .user(sessionUser)
                .board(board)
                .build();
        commentRepository.save(comment);
        log.info("댓글 작성 완료");

    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Integer id, UserResponse.SessionDTO sessionUser) {
        log.info("댓글 삭제 서비스 시작");

        // 댓글 조회
        Comment comment = commentRepository.findByIdJoinBoard(id)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다"));

        // 작성자인지 확인 - 작성자 아니면 예외
        if (!comment.getUser().equals(sessionUser.getId())) {
            throw new Exception403("본인이 작성한 댓글이 아닙니다");
        }
        commentRepository.deleteById(id);
        log.info("댓글 삭제 성공 - id :{}", id);


    }

}
