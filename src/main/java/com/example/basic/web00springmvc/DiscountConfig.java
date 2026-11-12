package com.example.basic.web00springmvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * [교안 4.2, 5.2] 자바 코드로 빈을 직접 등록하는 설정 클래스.
 * '@Bean' 메서드의 반환 객체가 빈이 되고, 메서드 이름이 빈 이름이 된다.
 */
@Configuration
public class DiscountConfig {

    @Bean
    @Primary                                  // 같은 타입이 여럿일 때 기본으로 주입할 빈 → 교안 5.2
    public DiscountPolicy fixDiscountPolicy() {
        return new FixDiscountPolicy();
    }

    @Bean
    public DiscountPolicy rateDiscountPolicy() {
        return new RateDiscountPolicy();
    }
}
