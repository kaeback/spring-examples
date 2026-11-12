package com.example.basic.web06filterexception.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * [교안 3.2] 인터셉터 (Spring Interceptor) — 디스패처 서블릿 '안쪽'에서 동작.
 *
 * <p>필터는 doFilter() 하나뿐이지만, 인터셉터는 단계별 콜백을 제공한다.
 *  - preHandle      : 컨트롤러 호출 '전'. false 를 반환하면 진행 중단.
 *  - postHandle     : 컨트롤러 호출 '후'(정상 시). 컨트롤러에서 예외가 나면 호출되지 않음.
 *  - afterCompletion: 요청 완료 후. 예외 발생 여부와 무관하게 '항상' 호출 → 로그/리소스 정리에 적합.
 */
@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // @GetMapping 등으로 처리되는 요청이면 handler 는 HandlerMethod 다.
        // (정적 리소스라면 ResourceHttpRequestHandler)
        if (handler instanceof HandlerMethod hm) {
            log.info("[인터셉터] preHandle       → {}#{}",
                    hm.getBeanType().getSimpleName(), hm.getMethod().getName());
        }
        return true;   // true: 계속 진행 / false: 여기서 중단
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) {
        // 컨트롤러가 정상 반환했을 때만 호출된다. 이동할 뷰 이름을 볼 수 있다.
        // (@RestController 처럼 뷰 없이 본문을 바로 쓰면 modelAndView 는 null)
        log.info("[인터셉터] postHandle      → view={}",
                modelAndView != null ? modelAndView.getViewName() : null);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        // 뷰 렌더링까지 끝난 뒤 항상 호출된다. 처리되지 않은 예외가 있으면 ex 로 받는다.
        // (String.valueOf 로 감싸지 않으면 로거가 ex 를 예외로 보고 스택트레이스 전체를 찍는다)
        log.info("[인터셉터] afterCompletion → status={}, ex={}", response.getStatus(), String.valueOf(ex));
    }
}
