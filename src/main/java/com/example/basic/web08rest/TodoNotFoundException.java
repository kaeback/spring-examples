package com.example.basic.web08rest;

/**
 * [교안 3.5] 없는 할 일을 요청했을 때 던지는 예외. TodoApiAdvice 가 404 JSON 으로 바꾼다.
 */
public class TodoNotFoundException extends RuntimeException {

    public TodoNotFoundException(Long id) {
        super("할 일이 없습니다. id=" + id);
    }
}
