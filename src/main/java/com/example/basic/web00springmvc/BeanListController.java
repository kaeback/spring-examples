package com.example.basic.web00springmvc;

import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * [교안 2.1, 2.3] 스프링 컨테이너(ApplicationContext)에 등록된 빈 중 이 패키지의 것만 보여 준다.
 * GET /ex00/beans → "빈 이름 → 클래스" 목록 (텍스트)
 */
@RestController
public class BeanListController {

    private final ApplicationContext context;   // 컨테이너 자신도 주입받을 수 있다

    public BeanListController(ApplicationContext context) {   // 생성자가 하나면 @Autowired 생략 가능
        this.context = context;
    }

    @GetMapping(value = "/ex00/beans", produces = "text/plain;charset=UTF-8")
    public String beans() {
        String myPackage = getClass().getPackageName();
        return Arrays.stream(context.getBeanDefinitionNames())
                .sorted()
                .filter(name -> {
                    Class<?> type = ClassUtils.getUserClass(context.getType(name));   // 프록시면 원래 클래스
                    return type.getPackageName().equals(myPackage);
                })
                .map(name -> name + " → " + ClassUtils.getUserClass(context.getType(name)).getSimpleName())
                .collect(Collectors.joining("\n"));
    }
}
