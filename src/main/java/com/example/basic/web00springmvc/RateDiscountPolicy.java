package com.example.basic.web00springmvc;

/**
 * [교안 4.2] 정률 할인 — 가격의 10%.
 * '@Component' 를 붙이지 않는다. DiscountConfig 의 '@Bean' 메서드가 직접 등록한다.
 */
public class RateDiscountPolicy implements DiscountPolicy {

    @Override
    public int discount(int price) {
        return price * 10 / 100;
    }
}
