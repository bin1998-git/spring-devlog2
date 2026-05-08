package com.example.demo3.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    // 게시글 번호로 댓글 목록 조회 - user 페치조인
    @Query("""
    SELECT c FROM Comment c JOIN FETCH c.user WHERE c.board.id = :boardId ORDER BY c.id DESC
""")
    List<Comment> findAllByBoardId(@Param("boardId") Integer boardId);

    // 댓글 단건 조회
    @Query("""
      SELECT c FROM Comment c JOIN FETCH c.user JOIN FETCH c.board WHERE c.id = :id
""")
    Optional<Comment> findByIdJoinBoard(@Param("id") Integer id);
}
