package com.example.demo3.comment;

import lombok.Getter;

public class CommentResponse {

    @Getter
    public static class DetailDTO {
        private Integer id;
        private String content;
        private UserInfo user;
        private Boolean isOwner;

        public DetailDTO(Comment comment, Integer sessionUserId) {
            this.id = comment.getId();
            this.content = comment.getContent();
            this.user = new UserInfo(comment.getUser().getUsername());
            this.isOwner = sessionUserId != null && comment.getUser().getId().equals(sessionUserId);
        }

        @Getter
        public static class UserInfo {
            private String username;

            public UserInfo(String username) {
                this.username = username;
            }
        }
    }
}