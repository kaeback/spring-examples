package com.example.basic.test03tdd;

import java.time.LocalTime;

/**
 * [테스트 3장 5.2] 고치기 전 — 메서드 안에서 현재 시각을 직접 읽는다.
 * 테스트 결과가 테스트를 실행한 시각에 따라 달라진다.
 */
public class OrderHoursBefore {

    /** 09:00부터 21:00 전까지 주문을 받는다 */
    public boolean isOpen() {
        LocalTime now = LocalTime.now();                   // 테스트에서 이 값을 바꿀 방법이 없다
        return !now.isBefore(LocalTime.of(9, 0)) && now.isBefore(LocalTime.of(21, 0));
    }
}
