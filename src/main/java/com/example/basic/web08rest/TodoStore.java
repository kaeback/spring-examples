package com.example.basic.web08rest;

import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * [교안 2.3] 메모리 저장소 — DB 없이 REST API 만 연습하려고 Map 에 저장한다.
 *
 * <p>싱글톤 빈 하나를 여러 요청(스레드)이 함께 쓰므로 ConcurrentHashMap·AtomicLong 을 쓴다.
 */
@Component
public class TodoStore {

    private final Map<Long, Todo> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();   // id 자동 증가

    public TodoStore() {
        save("REST API 장 읽기");             // 샘플 데이터 두 개
        save("curl 로 API 불러 보기");
    }

    public Todo save(String title) {
        Long id = sequence.incrementAndGet();
        Todo todo = new Todo(id, title);
        store.put(id, todo);
        return todo;
    }

    public Optional<Todo> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<Todo> findAll() {
        return store.values().stream()
                .sorted(Comparator.comparing(Todo::getId))   // id 순서대로
                .toList();
    }

    /** 지웠으면 true, 원래 없었으면 false */
    public boolean delete(Long id) {
        return store.remove(id) != null;
    }
}
