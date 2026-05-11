package com.example.demo3.board;

import lombok.Data;
import lombok.Getter;

public class BoardResponse {

    @Data
    public static class ListDTO {
        private Integer id;
        private String title;
        private String username;
        private String categoryName;
        private String createdAt;

        public ListDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            if (board.getUser() != null) {
                this.username = board.getUser().getUsername();
            }
            if (board.getCategory() != null) {
                this.categoryName = board.getCategory().getName();
            }
            if (board.getCreatedAt() != null) {
                this.createdAt = board.getForMatterCreatedAt();
            }
        }
    }

    // 게시글 상세보기 응답DTO
    @Data
    public static class DetailDTO {
        private Integer id; // board pk
        private String title;
        private String content;
        private String username;
        private Integer userId; // user pk
        private String categoryName;
        private String created;

        public DetailDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            if (board.getUser() != null) {
                this.username = board.getUser().getUsername();
                this.userId = board.getUser().getId();
            }
            if (board.getCategory() != null) {
                this.categoryName = board.getCategory().getName();
            }
            this.created = board.getCreatedAt() != null
                    ? board.getForMatterCreatedAt() : "";
        }
    }
}
