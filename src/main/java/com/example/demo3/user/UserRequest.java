package com.example.demo3.user;

import lombok.Builder;
import lombok.Data;

import java.lang.ref.PhantomReference;

public class UserRequest {


    // 회원가입 요청
    @Data
    @Builder
    public static class JoinDTO {
        private String username;
        private String password;
        private String email;

        // 매서드 편의기능
        public User toEntity() {
            return User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .build();


        }
        // 유효성 검사
        public void validate() {
            if (username == null || username.isBlank()) {
                throw new IllegalArgumentException("사용자 명은 필수 입력값 입니다");
            }
            if (password == null || password.isBlank()) {
                throw new IllegalArgumentException("비밀번호는 필수 입력값입니다");
            }
            if (email == null || email.isBlank()) {
                throw new IllegalArgumentException("이메일은 필수 입력값입니다");
            }
        }
    }




    // 로그인 요청
    @Data
    public static class LoginDTO{
        private String username;
        private String password;

        //  유효성 검사
        public void validate() {
            if (username == null || username.isBlank()) {
                throw new IllegalArgumentException("사용자 명을 필수입력하세요");
            }
            if (password == null || password.isBlank()) {
                throw new IllegalArgumentException("비밀번호를 필수 입력하세요");
            }
        }
    }


    // 회원정보 수정
    @Data
    public static class UpdateDTO {
        private String password;

        // 유효성 검사
        public void validate() {
            if (password == null || password.isBlank() || password.length() < 4) {
                throw new IllegalArgumentException("비밀번호를 필수이고 4자 이상이어야합니다");
            }
        }
    }

}
