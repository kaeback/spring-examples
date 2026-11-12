package com.example.basic.test03tdd;

/**
 * [테스트 3장 4.2] 장바구니에 담는 상품 한 줄 — 이름, 단가, 수량.
 */
public record Item(String name, int price, int quantity) {

    /** 이 줄의 금액 = 단가 × 수량 */
    public int amount() {
        return price * quantity;
    }
}
