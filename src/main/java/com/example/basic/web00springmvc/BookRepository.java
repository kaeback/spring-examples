package com.example.basic.web00springmvc;

import java.util.List;

/**
 * [교안 2.1] 저장소 역할(인터페이스). 서비스는 구현체가 아니라 이 역할에 의존한다.
 */
public interface BookRepository {
    Book save(Book book);
    List<Book> findAll();
}
