package com.example.demo3.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    // 전체 카테고리 목록 조회
    @Query("""
     SELECT c FROM Category c ORDER BY c.id DESC
""")
    List<Category> findAll();
}
