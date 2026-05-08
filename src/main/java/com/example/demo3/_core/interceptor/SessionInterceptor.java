package com.example.demo3._core.interceptor;

import com.example.demo3.user.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component // IoC처리
public class SessionInterceptor implements HandlerInterceptor {

    // 컨트롤러 로직이 거의 끝나는 시점 즉 화면이 그려지기 직전에
    // SessionUser 값을 주입 할 예정


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("SessionInterceptor 인터셉터 동작 확인");
        // HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);

        // 1. 화면을(View) 반환하는 요청인지 먼저 확인한다. /** <-- 모든 URL 요청시 동작
        // 데이터만(JSON) 반환 하는 요청일 경우 modelAndView가 없음으로 건너뜀
        if (modelAndView != null) {
            HttpSession session = request.getSession(false);
            UserResponse.SessionDTO sessionUser = null;
            if (session != null) {
                sessionUser = (UserResponse.SessionDTO) session.getAttribute("sessionUser");
            }
            // 로그인 여부와 무관하게 항상 주입 (null이면 {{#sessionUser}} 미출력)
            modelAndView.addObject("sessionUser", sessionUser);
        }

    }
}
