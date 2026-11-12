package com.example.basic.test03tdd;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * [테스트 3장 4.2] 상황별로 @Nested 클래스를 나누고, 테스트 데이터는 팩토리 메서드로 만든다.
 */
@DisplayName("장바구니")
class CartTest {

    @Nested
    @DisplayName("비어 있을 때")
    class 비어_있을_때 {

        Cart cart = new Cart();

        @Test
        @DisplayName("결제 금액은 0원이다 (배송비도 붙지 않는다)")
        void 결제_금액_0원() {
            assertThat(cart.paymentAmount()).isZero();
        }

        @Test
        @DisplayName("쿠폰을 적용할 수 없다")
        void 쿠폰_적용_불가() {
            assertThatThrownBy(() -> cart.applyCoupon(coupon(1_000, 0)))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("빈 장바구니");
        }
    }

    @Nested
    @DisplayName("상품 금액이 5만 원 미만이면")
    class 오만_원_미만 {

        Cart cart = cartWith(item(49_999));                  // 경계값 바로 아래

        @Test
        @DisplayName("배송비 3,000원이 붙는다")
        void 배송비_부과() {
            assertThat(cart.shippingFee()).isEqualTo(3_000);
            assertThat(cart.paymentAmount()).isEqualTo(52_999);
        }
    }

    @Nested
    @DisplayName("상품 금액이 5만 원 이상이면")
    class 오만_원_이상 {

        Cart cart = cartWith(item(30_000), item(20_000));    // 합계가 정확히 5만 원 (경계값)

        @Test
        @DisplayName("배송비가 무료다")
        void 배송비_무료() {
            assertThat(cart.shippingFee()).isZero();
            assertThat(cart.paymentAmount()).isEqualTo(50_000);
        }
    }

    @Nested
    @DisplayName("쿠폰을 적용하면")
    class 쿠폰을_적용하면 {

        @Test
        @DisplayName("최소 주문 금액 이상이면 할인 금액만큼 줄어든다")
        void 할인된다() {
            Cart cart = cartWith(item(60_000));

            cart.applyCoupon(coupon(5_000, 50_000));

            assertThat(cart.paymentAmount()).isEqualTo(55_000);
        }

        @Test
        @DisplayName("최소 주문 금액보다 적으면 예외가 발생하고 할인되지 않는다")
        void 최소_주문_금액_미만() {
            Cart cart = cartWith(item(40_000));

            assertThatThrownBy(() -> cart.applyCoupon(coupon(5_000, 50_000)))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("50000원");
            assertThat(cart.paymentAmount()).isEqualTo(43_000);  // 상품 40,000 + 배송비 3,000
        }
    }

    // ---- 테스트 데이터 팩토리: 이 테스트에서 중요한 값(가격·할인)만 드러나게 한다 ----

    private static Cart cartWith(Item... items) {
        Cart cart = new Cart();
        for (Item item : items) {
            cart.add(item);
        }
        return cart;
    }

    private static Item item(int price) {
        return new Item("상품" + price, price, 1);          // 이름은 서로 구분만 되면 된다
    }

    private static Coupon coupon(int discount, int minOrderAmount) {
        return new Coupon("테스트 쿠폰", discount, minOrderAmount);
    }
}
