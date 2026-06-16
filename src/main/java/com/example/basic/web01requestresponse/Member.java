package com.example.basic.web01requestresponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 요청·응답 실습용 간단 도메인.
 *
 * <p>@ModelAttribute(폼/쿼리 → 객체) 와 @RequestBody(JSON → 객체) 바인딩의 대상으로 쓴다.
 * 이 장은 DB를 쓰지 않으므로 JPA 엔티티가 아니라 평범한 자바 객체(POJO)다.
 */
@Data                 // getter/setter/toString 등 자동 생성 (Lombok)
@NoArgsConstructor    // @ModelAttribute·JSON 역직렬화는 기본 생성자가 필요
@AllArgsConstructor   // new Member(1L, "kim", 20) 형태로 간편 생성
public class Member {
    private Long id;
    private String name;
    private int age;
}
