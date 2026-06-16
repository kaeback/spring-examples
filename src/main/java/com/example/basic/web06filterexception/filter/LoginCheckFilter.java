package com.example.basic.web06filterexception.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * [교안 10] 필터로 로그인 체크 — 인증 안 된 요청을 서블릿(컨트롤러)에 닿기 전에 걸러낸다.
 *
 * <p>이 실습에서는 WebConfig 에서 "/ex06/filter-secure/*" 경로에만 등록한다.
 * 그래서 그 경로의 모든 요청은 세션 로그인이 있어야 통과한다.
 *
 * <p>교안의 whitelist 방식(특정 경로만 인증 제외)도 참고:
 *   private static final String[] whitelist = {"/", "/login", ...};
 *   필터를 "/*" 전체에 걸고, whitelist 에 없는 경로만 인증을 검사하는 방식이다.
 */
@Slf4j
public class LoginCheckFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();

        HttpSession session = httpRequest.getSession(false);   // 없으면 null (새로 만들지 않음)
        if (session == null || session.getAttribute("ex06Login") == null) {
            log.info("[필터] 미인증 사용자 요청 {}", requestURI);
            httpResponse.sendRedirect("/ex06/login");          // 로그인 페이지로
            return;                                            // return 하지 않으면 그대로 진행되니 주의!
        }

        chain.doFilter(request, response);   // 인증 통과 → 다음 단계로
    }
}
