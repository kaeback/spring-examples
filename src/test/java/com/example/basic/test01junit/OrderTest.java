package com.example.basic.test01junit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * [테스트 1장 2.5] 한 메서드(discount)를 '행동'마다 나눠 테스트한다 — 정상·경계·예외.
 */
@DisplayName("주문 할인")
class OrderTest {

    @Test
    @DisplayName("1만 원 미만은 할인이 없다")
    void 만원_미만은_할인_없음() {
        assertThat(new Order().discount(9_999)).isEqualTo(0);
    }

    @Test
    @DisplayName("정확히 1만 원부터 할인이 시작된다 (경계값)")
    void 정확히_만원부터_할인_시작() {
        assertThat(new Order().discount(10_000)).isEqualTo(1_000);
    }

    @Test
    @DisplayName("1만 원 이상은 10% 할인")
    void 만원_이상은_10퍼센트_할인() {
        assertThat(new Order().discount(20_000)).isEqualTo(2_000);
    }

    @Test
    @DisplayName("음수 가격은 예외")
    void 음수_가격은_예외() {
        assertThatThrownBy(() -> new Order().discount(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
