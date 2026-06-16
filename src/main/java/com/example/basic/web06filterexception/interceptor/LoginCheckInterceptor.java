package com.example.basic.web06filterexception.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * [교안 10] 인터셉터로 로그인 체크 (스프링 환경에서 권장되는 방식).
 *
 * <p>preHandle 에서 세션 로그인 여부만 확인하면 된다.
 * 인터셉터는 WebConfig 의 addPathPatterns/excludePathPatterns 로 URL 을 세밀하게 지정할 수 있어
 * 필터보다 인증 체크에 더 편하다. (여기서는 "/ex06/secure/**" 에만 적용)
 */
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("ex06Login") == null) {
            log.info("[인터셉터] 미인증 사용자 요청 {}", request.getRequestURI());
            response.sendRedirect("/ex06/login");
            return false;   // 컨트롤러로 진행하지 않고 여기서 중단
        }
        return true;
    }
}
