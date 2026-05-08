package com.example.demo3.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {


    // 아이디 중복 체크 - 회원가입 시 사용
    @Query("""
        SELECT u FROM User u WHERE u.username = :username
    """)
    Optional<User> findByUserName(@Param("username") String username);

    // 로그인 - 아이디와 비밀번호로 유저 조회
    @Query("""
        SELECT u FROM User u WHERE u.username = :username AND u.password = :password
""")
    Optional<User> findByUsernameAndPassword(@Param("username") String username,
                                             @Param("password") String password);
}
