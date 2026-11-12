package com.example.basic.web08rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

/**
 * [교안 3.2~3.4] 할 일 REST API — 화면(HTML) 대신 JSON 을 주고받는다.
 *
 * <pre>
 * GET    /ex08/api/todos        목록        200
 * GET    /ex08/api/todos/{id}   단건        200 / 404
 * POST   /ex08/api/todos        생성        201 + Location / 400
 * PUT    /ex08/api/todos/{id}   전체 수정   200 / 400 / 404
 * DELETE /ex08/api/todos/{id}   삭제        204 / 404
 * </pre>
 */
@RestController                      // = @Controller + @ResponseBody : 반환값이 곧 응답 본문(JSON)
@RequestMapping("/ex08/api/todos")   // 자원(todos)을 가리키는 URI. 동사는 HTTP 메서드로 표현한다
@RequiredArgsConstructor
public class TodoApiController {

    private final TodoStore todoStore;

    /** [교안 3.2] 목록 — List 를 반환하면 JSON 배열이 된다. 상태 코드는 기본값 200. */
    @GetMapping
    public List<TodoResponse> list() {
        return todoStore.findAll().stream()
                .map(TodoResponse::from)     // 엔티티 → DTO
                .toList();
    }

    /** [교안 3.2] 단건 — URI 의 {id} 를 @PathVariable 로 받는다. */
    @GetMapping("/{id}")
    public TodoResponse get(@PathVariable Long id) {
        return TodoResponse.from(findTodo(id));
    }

    /** [교안 3.3] 생성 — JSON 본문을 @RequestBody 로 받고, 201 Created + Location 헤더로 응답한다. */
    @PostMapping
    public ResponseEntity<TodoResponse> create(@Valid @RequestBody TodoCreateRequest request) {
        Todo saved = todoStore.save(request.title());
        URI location = URI.create("/ex08/api/todos/" + saved.getId());   // 새로 만든 자원의 주소
        return ResponseEntity.created(location).body(TodoResponse.from(saved));
    }

    /** [교안 3.4] 전체 수정 — 바뀐 자원을 200 으로 돌려준다. */
    @PutMapping("/{id}")
    public TodoResponse update(@PathVariable Long id, @Valid @RequestBody TodoUpdateRequest request) {
        Todo todo = findTodo(id);
        todo.update(request.title(), request.done());
        return TodoResponse.from(todo);
    }

    /** [교안 3.4] 삭제 — 돌려줄 본문이 없으므로 204 No Content. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!todoStore.delete(id)) {
            throw new TodoNotFoundException(id);   // 없는 자원 → 404 (TodoApiAdvice)
        }
        return ResponseEntity.noContent().build();
    }

    private Todo findTodo(Long id) {
        return todoStore.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(id));
    }
}
