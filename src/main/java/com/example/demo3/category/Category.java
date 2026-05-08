package com.example.demo3.category;

import com.example.demo3.board.Board;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "category_tb")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    // Category 기준 1:N → 한 카테고리에 여러 게시글
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Board> boards;
}