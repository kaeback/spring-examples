package com.example.basic.test01junit;

/**
 * [테스트 1장 3.2] 한 메서드에 확인할 '행동'이 여러 개인 예 — 1만 원 이상이면 10% 할인.
 */
public class Order {

    public int discount(int price) {
        if (price < 0) throw new IllegalArgumentException("가격은 음수일 수 없다");
        return price >= 10_000 ? (int) (price * 0.1) : 0;   // 1만원 이상만 10% 할인
    }
}
