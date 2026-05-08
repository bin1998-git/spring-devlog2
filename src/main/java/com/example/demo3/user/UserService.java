package com.example.demo3.user;

import com.example.demo3._core.errors.Exception400;
import com.example.demo3._core.errors.Exception404;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    // 회원가입 처리
    @Transactional
    public UserResponse.JoinDTO Join(UserRequest.JoinDTO joinDTO) {
        userRepository.findByUserName(joinDTO.getUsername()).ifPresent(user -> {
            log.warn("회원가입 실패 - 중복된 사용자명 : {}", user.getUsername());
            throw new RuntimeException("이미 존재하는 사용자 이름입니다");
        });
        User savedUser = userRepository.save(joinDTO.toEntity());
        log.info("회원가입 성공 - id : {}" ,
                savedUser.getId());
        return new UserResponse.JoinDTO(savedUser);
    }

    // 로그인 처리
    public UserResponse.SessionDTO login(UserRequest.LoginDTO loginDTO) {
        log.info("로그인 서비스 시작");
       User userEntity = userRepository.findByUsernameAndPassword(loginDTO.getUsername(), loginDTO.getPassword()).orElseThrow(() -> {
            log.warn("로그인 실패 - 사용자 이름 또는 비밀번호 잘못 입력");
            throw new Exception400("사용자명 또는 비밀번호가 올바르지 않습니다");
        });

        log.info("로그인 성공 - 사용자명 : {}", loginDTO.getUsername());
        return new UserResponse.SessionDTO(userEntity);
    }

    // 로그인한후 마이페이지
    public UserResponse.SessionDTO Mypage(Integer id, UserRequest.UpdateDTO updateDTO) {
        log.info("회원정보 수정 화면 서비스 시작");

        User userEntity = userRepository.findById(id).orElseThrow(() -> {
            throw new Exception404("사용자 정보를 불러올 수 없습니다");
        });

        return new UserResponse.SessionDTO(userEntity);
    }

    @Transactional
    // 회원정보 수정
    public UserResponse.SessionDTO UpdateUser(Integer id, UserRequest.UpdateDTO updateDTO, HttpSession session) {

        log.info("회원정보 수정 서비스 시작");

        User userEntity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다"));


        userEntity.update(updateDTO);

        log.info("회원정보 수정 완료 - id : {}", userEntity.getId());

        // 세션 동기화 - 수정된 정보를 세션에 반영
        UserResponse.SessionDTO sessionDTO = new UserResponse.SessionDTO(userEntity);
        session.setAttribute("sessionUser", sessionDTO);
        return sessionDTO;
    }



}
