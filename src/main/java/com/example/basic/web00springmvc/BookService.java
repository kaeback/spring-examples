package com.example.basic.web00springmvc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * [교안 2.4] 비즈니스 로직 계층 — 저장소(BookRepository)를 생성자로 주입받는다.
 */
@Service
@RequiredArgsConstructor          // final 필드를 받는 생성자 자동 생성 → 생성자 주입
public class BookService {

    private final BookRepository bookRepository;   // 구현체(MemoryBookRepository)는 스프링이 찾아 주입한다

    public Book register(String title, int price) {
        return bookRepository.save(new Book(title, price));
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}
