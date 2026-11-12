package com.example.basic.web02thymeleaf;

import org.springframework.stereotype.Component;

/**
 * [교안 2.1, 2.3] 타임리프에서 스프링 빈을 직접 호출하는 예제용 빈.
 *
 * <p>템플릿에서 ${@helloBean.hello('Spring!')} 처럼 빈 이름(@helloBean)으로 메서드를 호출할 수 있다.
 */
@Component("helloBean")
public class HelloBean {
    public String hello(String data) {
        return "Hello " + data;
    }
}
