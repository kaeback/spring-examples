package com.example.basic.web00springmvc;

/**
 * [교안 4.2] 할인 정책 역할. 같은 타입(DiscountPolicy)의 구현체가 두 개 있다.
 */
public interface DiscountPolicy {
    /** 할인 금액을 돌려준다. */
    int discount(int price);
}
