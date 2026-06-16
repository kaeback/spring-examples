package com.example.basic;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 실습 허브 — 각 장(ex01~ex07)으로 가는 링크를 모은 첫 화면.
 * 실행 후 http://localhost:8080/ 으로 접속한다.
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }
}
