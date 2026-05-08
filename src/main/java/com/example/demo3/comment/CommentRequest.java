package com.example.demo3.comment;

import lombok.Data;

public class CommentRequest {
    @Data
    public static class SaveDTO {
        private String content;
        private Integer boardId;

        // 유효성 검사
        public void validate() {
            if (content == null || content.isBlank()) {
                throw new IllegalArgumentException("댓글 내용은 필수 입력값입니다");
            }
            if (boardId == null) {
                throw new IllegalArgumentException("게시글 정보가 없습니다");
            }
        }

    }
}
