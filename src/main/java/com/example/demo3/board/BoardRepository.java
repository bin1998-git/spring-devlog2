package com.example.demo3.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Integer> {

    // BoardRepository 에 추가
    @Query("""
    SELECT b FROM Board b JOIN FETCH b.user JOIN FETCH b.category 
    WHERE b.category.name = :categoryName ORDER BY b.id DESC
""")
    List<Board> findAllByCategory(@Param("categoryName") String categoryName);

    // 단건조회
    @Query("""
        SELECT b FROM Board b JOIN FETCH b.user LEFT JOIN FETCH b.category WHERE b.id =:id
""")
    Optional<Board> findByIdJoinUser(@Param("id") Integer id);

    // 전체게시글조회
    @Query("""
        SELECT b FROM Board b JOIN FETCH b.user LEFT JOIN FETCH b.category ORDER BY b.id DESC
""")
    List<Board> findAllJoinUser();
}
