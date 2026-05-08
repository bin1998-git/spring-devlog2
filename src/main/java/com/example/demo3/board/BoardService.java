package com.example.demo3.board;

import com.example.demo3.category.Category;
import com.example.demo3.category.CategoryRepository;
import com.example.demo3.user.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final CategoryRepository categoryRepository;

    // 게시글 작성
    @Transactional
    public void writePost(BoardRequest.SaveDTO saveDTO, UserResponse.SessionDTO sessionUser) {
        log.info("게시글 작성 서비스 시작");

            // 카테고리 조회 - 없으면 예외 발생
            Category category = categoryRepository.findById(saveDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다"));

            // 빌더 패턴으로 엔티티 생성 후 저장
            Board board = Board.builder()
                    .title(saveDTO.getTitle())
                    .content(saveDTO.getContent())
                    .user(sessionUser)
                    .category(category)
                    .build();
            boardRepository.save(board);
            log.info("게시글 작성 완료 - id : {}",board.getId());

    }

    // 게시글 목록
    public List<BoardResponse.ListDTO> postList() {
        log.info("게시글 목록 조회 서비스 시작");

        return boardRepository.findAllJoinUser()
                .stream()
                .map(BoardResponse.ListDTO::new)
                .collect(Collectors.toList());
    }

    // 게시글 상세 조회
    public BoardResponse.DetailDTO postDetails(Integer id) {

        log.info("게시글 상세 조회 서비스 시작 - id : {}", id);

        Board board = boardRepository.findByIdJoinUser(id)
                .orElseThrow(() -> {
                    log.warn("게시글 조회 실패 - id : {}", id);
                    return new RuntimeException("게시글을 찾을 수 없습니다");
                });

        log.info("게시글 상세 조회 완료 - id : {}", id);
        return new BoardResponse.DetailDTO(board);
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Integer id, UserResponse.SessionDTO sessionUser) {
        log.info("게시글 삭제 서비스 시작");

        Board board = boardRepository.findByIdJoinUser(id).orElseThrow(() -> {
            throw new RuntimeException("게시글을 찾을 수 없습니다");
        });

        // 작성자 아니면 예외발생
        board.isOwner(sessionUser.getId());
        boardRepository.deleteById(id);
        log.info("게시글 삭제 성공 - id : {} ", id);
    }

    // 게시글 수정
    @Transactional
    public void updatePost(Integer id, BoardRequest.UpdateDTO updateDTO, UserResponse.SessionDTO sessionUser) {
        log.info("게시글 수정 서비스 시작");

        Board board = boardRepository.findByIdJoinUser(id).orElseThrow(() -> {
            throw new RuntimeException("해당 게시글을 찾을 수 없습니다");
        });
        // 작성자 아니면 예외발생
        board.isOwner(sessionUser.getId());
        board.update(updateDTO);
        log.info("수정 완료 - id : {}", id);
    }


}
