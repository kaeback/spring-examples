package com.example.basic.test01junit;

/**
 * [테스트 1장 2.2] 테스트 대상 — 스프링과 무관한 순수 자바 클래스.
 */
public class Calculator {

    public int add(int a, int b) {
        return a + b;
    }

    public int divide(int a, int b) {
        if (b == 0) {
            // int를 0으로 나누면 ArithmeticException이 나지만, 의미가 분명한 예외로 바꿔 던진다
            throw new IllegalArgumentException("0으로 나눌 수 없습니다");
        }
        return a / b;
    }
}
