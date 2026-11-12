package com.example.basic.web00springmvc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * [교안 2.4] 앱이 시작될 때 샘플 책 두 권을 넣어 둔다.
 * CommandLineRunner 빈은 컨테이너 준비가 끝난 직후 run() 이 한 번 호출된다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BookDataInit implements CommandLineRunner {

    private final BookService bookService;   // 이 클래스도 빈이므로 다른 빈을 주입받을 수 있다

    @Override
    public void run(String... args) {
        bookService.register("자바의 정석", 30000);
        bookService.register("스프링 입문", 25000);
        log.info("[ex00] 샘플 책 {}권 저장 완료", bookService.findAll().size());
    }
}
