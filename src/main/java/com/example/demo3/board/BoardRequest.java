package com.example.demo3.board;

import com.example.demo3.user.User;
import com.example.demo3.util.MyDateUtil;
import lombok.Builder;
import lombok.Data;

public class BoardRequest {

    @Data
    @Builder
    // 게시글 작성요청DTO
    public static class SaveDTO {
        private String title;
        private String content;
        private Integer categoryId;


        public Board toEntity(User user) {
            return Board.builder()
                    .title(title)
                    .content(content)
                    .user(user)
                    .build();
        }

        // 글 저장 유효성 검사
        public void validate() {
            if (content == null || content.length() < 3) {
                throw new RuntimeException("글 내용은 3글자 이상이어야 합니다");
            }
            if (title == null || title.isBlank()) {
                throw new RuntimeException("제목은 필수 입력 입니다");
            }
            if (categoryId == null) {
                throw new IllegalArgumentException("카테고리를 선택해주세요");
            }
        }

    }


    @Data
    @Builder
    // 게시글 수정요청DTO
    public static class UpdateDTO {
        private String title;
        private String content;
        private String categoryId;


        // 게시글 유효성 검사
        public void validate() {
            if (title == null || title.isBlank()) {
                throw new RuntimeException("제목은 빈칸일 수 없습니다");
            }
            if (content == null || content.isBlank() || content.length() < 3) {
                throw new RuntimeException("내용은 빈칸이거나 3글자 이상 작성해 주세요");
            }
            if (categoryId == null) {
                throw new IllegalArgumentException("카테고리를 선택해주세요");
            }
        }
    }

    @Data
    public static class DetailDTO {
        private Integer id;
        private String title;
        private String content;
        private String username;
        private Integer userId;
        private String categoryName;
        private String createdAt;
        private boolean isOwner;

        public DetailDTO(Board board, Integer sessionUserId) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            // 방어적 코드 - user null 체크
            if (board.getUser() != null) {
                this.username = board.getUser().getUsername();
                this.userId = board.getUser().getId();
            }
            // 방어적 코드 - category null 체크
            if (board.getCategory() != null) {
                this.categoryName = board.getCategory().getName();
            }
            // 방어적 코드 - createdAt null 체크
            if (board.getCreatedAt() != null) {
                this.createdAt = board.getForMatterCreatedAt();
            }
            // 로그인 안 한 경우 null 처리 - 소유자 아님
            if (sessionUserId != null && board.getUser() != null) {
                this.isOwner = board.getUser().getId().equals(sessionUserId);
            }
        }
    }
}
