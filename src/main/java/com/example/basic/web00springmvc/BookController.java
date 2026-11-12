package com.example.basic.web00springmvc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * [교안 2.1] 웹 계층 — 요청을 받아 서비스에 위임하고, 결과를 Model 에 담아 뷰를 고른다.
 */
@Controller
@RequestMapping("/ex00")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;   // 서비스에 위임

    @GetMapping("/books")
    public String list(Model model) {
        model.addAttribute("books", bookService.findAll());   // Model: 데이터를 담고
        return "ex00/books";                                   // View: templates/ex00/books.html
    }
}
