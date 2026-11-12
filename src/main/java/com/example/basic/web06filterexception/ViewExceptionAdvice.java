package com.example.basic.web06filterexception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * [교안 2.5] @ControllerAdvice — 여러 컨트롤러에 같은 예외 처리를 건다.
 *
 * <p>대상을 지정하지 않으면 애플리케이션의 모든 컨트롤러에 적용된다.
 * 이 실습 프로젝트는 여러 장이 한 앱에 모여 있으므로 대상을 ExceptionHandlerController 로 좁혔다.
 * (basePackages = "...", annotations = RestController.class 처럼 지정할 수도 있다.)
 */
@ControllerAdvice(assignableTypes = ExceptionHandlerController.class)
public class ViewExceptionAdvice {

    @ExceptionHandler(UnsupportedOperationException.class)
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)        // 응답 코드 501
    public String handleUnsupported(UnsupportedOperationException e, Model model) {
        model.addAttribute("message", "아직 지원하지 않는 기능입니다: " + e.getMessage());
        return "ex06/message";
    }
}
