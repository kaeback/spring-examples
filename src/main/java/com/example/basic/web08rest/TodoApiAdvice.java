package com.example.basic.web08rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * [교안 2.5] web08rest 패키지 전용 API 예외 처리 — 오류도 JSON({"code","message"})으로 내려 준다.
 *
 * <p>basePackages 로 이 패키지의 컨트롤러에만 적용했다. 다른 장의 컨트롤러(화면·오류 페이지 실습)에는
 * 영향을 주지 않는다. (원리는 5장 2.5~2.6 의 @ControllerAdvice·@RestControllerAdvice 와 같다)
 */
@RestControllerAdvice(basePackages = "com.example.basic.web08rest")
public class TodoApiAdvice {

    /** 없는 할 일 → 404 */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TodoNotFoundException.class)
    public ErrorResult notFound(TodoNotFoundException e) {
        return new ErrorResult("NOT_FOUND", e.getMessage());
    }

    /** @Valid 검증 실패 → 400 (첫 번째 오류 메시지만) */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResult invalid(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().getFirst().getDefaultMessage();
        return new ErrorResult("VALIDATION", message);
    }

    /** 본문이 없거나 JSON 문법이 틀림 → 400 (검증 이전 단계, JSON → 객체 변환 실패) */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorResult unreadable(HttpMessageNotReadableException e) {
        return new ErrorResult("BAD_JSON", "요청 본문(JSON)을 읽을 수 없습니다.");
    }
}
