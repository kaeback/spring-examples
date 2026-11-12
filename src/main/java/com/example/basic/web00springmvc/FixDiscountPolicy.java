package com.example.basic.web00springmvc;

/**
 * [교안 4.2] 정액 할인 — 가격과 상관없이 1000원.
 * '@Component' 를 붙이지 않는다. DiscountConfig 의 '@Bean' 메서드가 직접 등록한다.
 */
public class FixDiscountPolicy implements DiscountPolicy {

    @Override
    public int discount(int price) {
        return 1000;
    }
}
