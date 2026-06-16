package com.example.basic.web06filterexception.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

/**
 * [교안 10] 필터 (Servlet Filter) — 모든 요청에 공통으로 적용되는 로그 필터.
 *
 * <p>실행 위치: HTTP 요청 → WAS → [필터] → 서블릿(DispatcherServlet) → 컨트롤러
 * <p>필터는 doFilter() 하나로 동작하며, chain.doFilter() 를 호출해야 다음 단계로 진행된다.
 * (호출하지 않으면 요청이 거기서 끊긴다 → '필터로 걸러내기'에 활용)
 */
@Slf4j
public class LogFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        String uuid = UUID.randomUUID().toString();   // 요청 하나를 추적하기 위한 식별자

        try {
            log.info("REQUEST  [{}][{}]", uuid, requestURI);
            chain.doFilter(request, response);         // 다음 필터(없으면 서블릿) 호출
        } catch (Exception e) {
            throw e;
        } finally {
            log.info("RESPONSE [{}][{}]", uuid, requestURI);
        }
    }
}
