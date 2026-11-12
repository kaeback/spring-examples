package com.example.basic.web06filterexception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * [교안 2.6] @RestControllerAdvice — 여러 컨트롤러의 예외를 한곳에서 JSON 으로 변환.
 *
 * <p>@RestControllerAdvice = @ControllerAdvice + @ResponseBody.
 * 예외 종류별로 @ExceptionHandler 메서드를 두고, 알맞은 HTTP 상태와 오류 DTO 를 반환한다.
 *
 * <p>여기서는 assignableTypes 로 'API 컨트롤러(ApiExceptionController)'에만 적용했다.
 * 아래의 Exception 안전망은 모든 예외를 JSON 500 으로 바꾸므로, 대상을 넓히면 화면 컨트롤러
 * (ErrorPageController 등)의 예외까지 가로채 오류 페이지 실습이 동작하지 않는다.
 * (대상을 아예 빼면 애플리케이션 전체 컨트롤러에 적용된다.)
 */
@Slf4j
@RestControllerAdvice(assignableTypes = ApiExceptionController.class)
public class ExControllerAdvice {

    /** 잘못된 요청 → 400. (예: 존재하지 않는 회원 조회) */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e) {
        log.warn("[예외] IllegalArgumentException: {}", e.getMessage());
        return new ErrorResult("BAD", e.getMessage());
    }

    /** 커스텀 비즈니스 예외 → 404. 업무 상황별 예외를 알맞은 상태코드로 변환하는 예. */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MemberNotFoundException.class)
    public ErrorResult notFoundHandler(MemberNotFoundException e) {
        log.warn("[예외] MemberNotFoundException: {}", e.getMessage());
        return new ErrorResult("NOT_FOUND", e.getMessage());
    }

    /**
     * @RequestBody + @Valid 검증 실패 → 400.
     * (폼 검증의 BindingResult 와 달리, API 검증 실패는 MethodArgumentNotValidException 으로 던져진다.)
     * 어떤 필드가 왜 틀렸는지 모아서 메시지로 만든다.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResult validationHandler(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return new ErrorResult("VALIDATION", message);
    }

    /** 그 외 모든 예외 → 500 (마지막 안전망). */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResult exHandler(Exception e) {
        log.error("[예외] 처리되지 않은 예외", e);
        return new ErrorResult("INTERNAL", "내부 오류가 발생했습니다.");
    }
}
