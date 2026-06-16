package com.example.basic.web02thymeleaf;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 타임리프 변수 표현식(${...}) 실습용 객체.
 */
@Data
@AllArgsConstructor
public class User {
    private String username;
    private int age;
}
