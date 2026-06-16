package com.example.basic.web06filterexception.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

/**
 * [교안 10] 인터셉터 (Spring Interceptor) — 디스패처 서블릿 '안쪽'에서 동작.
 *
 * <p>필터는 doFilter() 하나뿐이지만, 인터셉터는 단계별 콜백을 제공한다.
 *  - preHandle      : 컨트롤러 호출 '전'. false 를 반환하면 진행 중단.
 *  - postHandle     : 컨트롤러 호출 '후'(정상 시). 컨트롤러에서 예외가 나면 호출되지 않음.
 *  - afterCompletion: 요청 완료 후. 예외 발생 여부와 무관하게 '항상' 호출 → 로그/리소스 정리에 적합.
 */
@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    public static final String LOG_ID = "logId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();
        request.setAttribute(LOG_ID, uuid);   // afterCompletion 까지 값을 넘기려면 request 에 저장

        // @RequestMapping 핸들러는 HandlerMethod, 정적 리소스는 ResourceHttpRequestHandler 다.
        if (handler instanceof HandlerMethod hm) {
            // 컨트롤러 메서드 정보(hm.getMethod() 등)에 접근할 수 있다.
            log.info("[인터셉터] 핸들러 메서드 = {}", hm.getMethod().getName());
        }

        log.info("[인터셉터] preHandle  [{}][{}]", uuid, requestURI);
        return true;   // false 면 컨트롤러로 진행하지 않고 중단
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) {
        // 컨트롤러에서 예외가 발생하면 이 메서드는 호출되지 않는다.
        log.info("[인터셉터] postHandle [{}]", modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        String logId = (String) request.getAttribute(LOG_ID);
        log.info("[인터셉터] afterCompletion [{}][{}]", logId, request.getRequestURI());
        if (ex != null) {
            log.error("[인터셉터] afterCompletion 에서 예외 감지!", ex);  // 예외와 무관하게 항상 호출됨
        }
    }
}
