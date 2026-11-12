package com.example.basic.web08rest;

/**
 * [교안 3.5] API 오류 응답(JSON).
 * <pre>{"code":"NOT_FOUND","message":"할 일이 없습니다. id=99"}</pre>
 */
public record ErrorResult(String code, String message) {
}
