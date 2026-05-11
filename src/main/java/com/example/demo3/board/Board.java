package com.example.demo3.board;

import com.example.demo3._core.errors.Exception403;
import com.example.demo3.category.Category;
import com.example.demo3.user.User;
import com.example.demo3.util.MyDateUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "board_tb")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @CreationTimestamp
    private Timestamp createdAt;

    public String getForMatterCreatedAt() {
        if (createdAt == null) return "";
        return MyDateUtil.timeStampFormat(createdAt);
    }

    public void update(BoardRequest.UpdateDTO updateDTO) {
        this.title = updateDTO.getTitle();
        this.content = updateDTO.getContent();
    }

    public boolean isOwner(Integer sessionUserId) {
        if (!this.user.getId().equals(sessionUserId)) {
            throw new Exception403("본인이 작성한 게시글이 아닙니다");
        }
        return true;
    }


}