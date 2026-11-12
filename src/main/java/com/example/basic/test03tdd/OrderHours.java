package com.example.basic.test03tdd;

import java.time.Clock;
import java.time.LocalTime;

/**
 * [테스트 3장 5.2] 고친 뒤 — 시계(Clock)를 생성자로 받는다.
 * 운영에서는 시스템 시계를, 테스트에서는 원하는 시각에 고정한 시계를 넣는다.
 */
public class OrderHours {

    static final LocalTime OPEN = LocalTime.of(9, 0);
    static final LocalTime CLOSE = LocalTime.of(21, 0);

    private final Clock clock;

    public OrderHours(Clock clock) {
        this.clock = clock;
    }

    /** 09:00부터 21:00 전까지 주문을 받는다 */
    public boolean isOpen() {
        LocalTime now = LocalTime.now(clock);              // 주입받은 시계로 현재 시각을 읽는다
        return !now.isBefore(OPEN) && now.isBefore(CLOSE);
    }
}
