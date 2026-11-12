package com.example.basic.web08rest;

/**
 * [교안 2.1] 응답 DTO — API 가 내보내는 JSON 의 모양을 정한다.
 * <pre>{"id":1,"title":"REST API 장 읽기","done":false}</pre>
 *
 * <p>엔티티(여기서는 Todo)를 그대로 반환하지 않고, 내보낼 값만 골라 담는다.
 */
public record TodoResponse(Long id, String title, boolean done) {

    public static TodoResponse from(Todo todo) {
        return new TodoResponse(todo.getId(), todo.getTitle(), todo.isDone());
    }
}
