package com.example.basic.web00springmvc;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * [교안 2.1] 데이터 접근 계층 — DB 대신 메모리(Map)에 저장하는 구현체.
 * '@Repository' 가 붙어 있어 컴포넌트 스캔으로 빈 등록된다. (빈 이름: memoryBookRepository)
 */
@Repository
public class MemoryBookRepository implements BookRepository {

    private final Map<Long, Book> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    @Override
    public Book save(Book book) {
        book.setId(sequence.incrementAndGet());   // id 자동 증가
        store.put(book.getId(), book);
        return book;
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(store.values());
    }
}
