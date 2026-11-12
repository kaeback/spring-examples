package com.example.basic.web00springmvc;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * [교안 5.2] 같은 타입의 빈이 두 개일 때 어떤 빈이 주입되는지 확인한다.
 * GET /ex00/discount
 */
@RestController
public class DiscountController {

    private final DiscountPolicy defaultPolicy;   // @Primary 빈이 들어온다
    private final DiscountPolicy ratePolicy;      // @Qualifier 로 빈 이름을 지정한다

    // @Qualifier 는 생성자 파라미터에 붙인다.
    // (Lombok @RequiredArgsConstructor 는 필드의 @Qualifier 를 생성자로 옮겨 주지 않으므로 생성자를 직접 쓴다)
    public DiscountController(DiscountPolicy defaultPolicy,
                              @Qualifier("rateDiscountPolicy") DiscountPolicy ratePolicy) {
        this.defaultPolicy = defaultPolicy;
        this.ratePolicy = ratePolicy;
    }

    @GetMapping(value = "/ex00/discount", produces = "text/plain;charset=UTF-8")
    public String discount() {
        int price = 20000;
        return "정가 " + price + "원\n"
                + "기본(@Primary)  " + defaultPolicy.getClass().getSimpleName()
                + " → " + defaultPolicy.discount(price) + "원 할인\n"
                + "@Qualifier 지정 " + ratePolicy.getClass().getSimpleName()
                + " → " + ratePolicy.discount(price) + "원 할인";
    }
}
