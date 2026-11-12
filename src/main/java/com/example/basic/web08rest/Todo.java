package com.example.basic.web08rest;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * [교안 2.1] 할 일(Todo) — 이 장의 API 가 다루는 '자원(resource)'.
 *
 * <p>DB 대신 TodoStore 의 Map 에 보관한다(앱을 끄면 사라진다).
 * 컨트롤러는 이 객체를 그대로 응답하지 않고 TodoResponse(DTO)로 바꿔 내보낸다.
 */
@Getter
public class Todo {

    private final Long id;
    private String title;                     // 할 일 내용
    private boolean done;                     // 완료 여부
    private final LocalDateTime createdAt;    // 내부 관리용 — API 응답에는 넣지 않는다

    public Todo(Long id, String title) {
        this.id = id;
        this.title = title;
        this.done = false;                    // 새 할 일은 '미완료'
        this.createdAt = LocalDateTime.now();
    }

    /** PUT 요청으로 내용 전체를 바꾼다. */
    public void update(String title, boolean done) {
        this.title = title;
        this.done = done;
    }
}
