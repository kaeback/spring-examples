package com.example.basic.web00springmvc;

import lombok.Getter;
import lombok.Setter;

/**
 * [교안 2.1] 책 도메인 객체 — DB 없이 메모리에 보관하는 평범한 자바 객체(POJO).
 */
@Getter
@Setter
public class Book {

    private Long id;        // 저장소가 부여하는 식별자
    private String title;   // 제목
    private int price;      // 가격

    public Book(String title, int price) {
        this.title = title;
        this.price = price;
    }
}
