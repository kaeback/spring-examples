package com.example.basic.web06filterexception.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

/**
 * [교안 2.2] 필터 (Servlet Filter) — 요청의 시작과 끝을 로그로 남긴다.
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

        HttpServletRequest httpRequest = (HttpServletRequest) request;   // HTTP 기능을 쓰려면 다운캐스팅
        String requestURI = httpRequest.getRequestURI();
        String uuid = UUID.randomUUID().toString().substring(0, 8);       // 요청 하나를 구분하는 짧은 ID

        try {
            log.info("[필터] REQUEST  [{}] {} {}", uuid, httpRequest.getMethod(), requestURI);
            chain.doFilter(request, response);    // 다음 필터(없으면 서블릿) 호출
        } finally {                               // 예외가 나도 응답 로그는 남긴다 (예외는 삼키지 않고 위로 전달)
            log.info("[필터] RESPONSE [{}] {}", uuid, requestURI);
        }
    }
}
