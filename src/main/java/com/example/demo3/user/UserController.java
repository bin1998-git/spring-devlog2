package com.example.demo3.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 회원가입 화면 요청
    @GetMapping("/join-form")
    public String joinFormPage() {
        return "user/join-form";
    }

    // 회원가입 기능 요청
    @PostMapping("/join")
    public String joinProc(UserRequest.JoinDTO joinDTO) {
        // 유효성 검사
        joinDTO.validate();
        userService.Join(joinDTO);
        return "redirect:/login-form";
    }


    // 로그인후 마이페이지
    @GetMapping("/user/update-form")
    public String Mypage(HttpSession session, Model model) {
        // 로그인한 상태의 마이페이지라서 인증검사 안해도 됨
        UserResponse.SessionDTO sessionDTO = (UserResponse.SessionDTO) session.getAttribute("sessionUser");
        model.addAttribute("user", sessionDTO);
        return "user/update-form";
    }

    // 로그인 화면 요청
    @GetMapping("/login-form")
    public String loginFormPage() {
        return "user/login-form";
    }

    // 로그인 기능 요청
    @PostMapping("/login")
    public String loginProc(UserRequest.LoginDTO loginDTO, HttpSession session) {
        loginDTO.validate();
        UserResponse.SessionDTO sessionDTO = userService.login(loginDTO);

        session.setAttribute("sessionUser", sessionDTO);
        return "redirect:/";
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }





    // 회원정보 수정 기능
    @PostMapping("/update")
    public String updateProc(UserRequest.UpdateDTO updateDTO, HttpSession session) {
        // 유효성 검사
        updateDTO.validate();
        UserResponse.SessionDTO sessionDTO = (UserResponse.SessionDTO) session.getAttribute("sessionUser");
        userService.UpdateUser(sessionDTO.getId(), updateDTO, session);
        return "redirect:/";
    }


}
