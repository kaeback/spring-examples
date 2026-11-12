package com.example.basic.test03tdd;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * [테스트 3장 5.2] 고정된 시계(Clock.fixed)를 넣어, 언제 실행해도 결과가 같은 테스트를 만든다.
 */
@DisplayName("주문 가능 시간")
class OrderHoursTest {

    static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");

    @ParameterizedTest(name = "{0} → 주문 가능")
    @ValueSource(strings = {"09:00", "13:30", "20:59"})
    void 영업_시간에는_주문_가능(String time) {
        OrderHours hours = new OrderHours(clockAt(time));

        assertThat(hours.isOpen()).isTrue();
    }

    @ParameterizedTest(name = "{0} → 주문 불가")
    @ValueSource(strings = {"08:59", "21:00", "23:30"})
    void 영업_시간_밖에는_주문_불가(String time) {
        OrderHours hours = new OrderHours(clockAt(time));

        assertThat(hours.isOpen()).isFalse();
    }

    /** 2026년 11월 12일, 주어진 시각에 멈춰 있는 시계 */
    private static Clock clockAt(String time) {
        LocalTime localTime = LocalTime.parse(time);
        return Clock.fixed(LocalDate.of(2026, 11, 12).atTime(localTime).atZone(SEOUL).toInstant(), SEOUL);
    }
}
