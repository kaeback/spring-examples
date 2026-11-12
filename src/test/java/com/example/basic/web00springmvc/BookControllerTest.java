package com.example.basic.web00springmvc;

import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * [교안 3.2] 생성자 주입이면 스프링 없이 new 로 만들고 가짜(Fake) 객체를 넣어 테스트할 수 있다.
 */
class BookControllerTest {

    /** 가짜 서비스 — BookService 를 상속해 저장소 없이 정해진 값만 돌려준다. */
    static class FakeBookService extends BookService {
        FakeBookService() {
            super(null);                      // 저장소는 쓰지 않으므로 null
        }

        @Override
        public List<Book> findAll() {
            return List.of(new Book("가짜 책", 100));
        }
    }

    @Test
    void 책_목록() {
        BookController controller = new BookController(new FakeBookService());   // 스프링 없이 직접 생성
        Model model = new ConcurrentModel();                                     // Model 구현체도 직접 만든다

        String view = controller.list(model);

        assertThat(view).isEqualTo("ex00/books");
        List<Book> books = (List<Book>) model.getAttribute("books");
        assertThat(books).extracting(Book::getTitle).containsExactly("가짜 책");
    }
}
