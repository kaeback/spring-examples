package com.example.basic.test03tdd;

/**
 * [테스트 3장 4.2] 정액 할인 쿠폰 — 상품 금액이 최소 주문 금액 이상일 때만 쓸 수 있다.
 */
public record Coupon(String name, int discount, int minOrderAmount) {
}
