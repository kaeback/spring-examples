package com.example.basic.test03tdd;

import java.util.ArrayList;
import java.util.List;

/**
 * [테스트 3장 4.2] 장바구니 — 상품 금액, 배송비, 쿠폰 할인으로 결제 금액을 계산한다.
 * 상황(빈 장바구니, 5만 원 미만·이상, 쿠폰 적용)마다 결과가 달라서 @Nested로 묶어 테스트한다.
 */
public class Cart {

    static final int FREE_SHIPPING_THRESHOLD = 50_000;   // 상품 금액이 이 금액 이상이면 배송비 무료
    static final int SHIPPING_FEE = 3_000;

    private final List<Item> items = new ArrayList<>();
    private Coupon coupon;                                 // 적용한 쿠폰 (없으면 null)

    public void add(Item item) {
        items.add(item);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    /** 상품 금액 합계 */
    public int itemsTotal() {
        return items.stream().mapToInt(Item::amount).sum();
    }

    /** 배송비 — 빈 장바구니는 0원, 상품 금액이 5만 원 이상이면 무료 */
    public int shippingFee() {
        if (isEmpty() || itemsTotal() >= FREE_SHIPPING_THRESHOLD) {
            return 0;
        }
        return SHIPPING_FEE;
    }

    public void applyCoupon(Coupon coupon) {
        if (isEmpty()) {
            throw new IllegalStateException("빈 장바구니에는 쿠폰을 적용할 수 없습니다");
        }
        if (itemsTotal() < coupon.minOrderAmount()) {
            throw new IllegalStateException("최소 주문 금액(" + coupon.minOrderAmount() + "원)보다 적습니다");
        }
        this.coupon = coupon;
    }

    /** 결제 금액 = 상품 금액 - 쿠폰 할인 + 배송비 */
    public int paymentAmount() {
        int discount = (coupon == null) ? 0 : coupon.discount();
        return itemsTotal() - discount + shippingFee();
    }
}
