# spring-examples — 웹 개발 실습

자바 백엔드 강의(**스프링 기반과 DI ~ Spring Security**)의 수업용 **예제** 프로젝트입니다.
교안 각 장의 "[예제]" 절이 이 프로젝트의 코드를 그대로 싣고 있습니다. (게시판 실습은 별도 프로젝트)
각 파트를 **패키지별로 분리**하고, 간단한 예제와 상세한 한글 주석으로 설명했습니다.

- Spring Boot 4.1.x (Spring Framework 7) / Java 21 / Gradle 9
- DB: **H2 인메모리**(별도 설치 불필요) · 화면: **Thymeleaf** · 검증: **Bean Validation** · 보안: **Spring Security**(`/ex10`만)

## 실행

```bash
./gradlew bootRun
```

- 브라우저 허브: <http://localhost:8080/>
- H2 콘솔: <http://localhost:8080/h2-console> (JDBC URL: `jdbc:h2:mem:testdb`, 사용자 `sa`)
- **web04 / web05(JPA) 데모는 앱이 켜질 때 콘솔에 자동 출력**됩니다. 함께 찍히는 SQL 로그를 관찰하세요.

## 실습 진행 순서

아래 번호 순서대로 진행한다. 패키지 번호 = URL 번호(`webNN` ↔ `/exNN`)로 맞춰 두었다.

0. **스프링 기반과 DI** `web00` `/ex00` — 계층(컨트롤러→서비스→리포지토리)·생성자 주입 → `@Bean` 직접 등록 → `@Primary`·`@Qualifier`
1. **요청과 응답** `web01` `/ex01` — 요청 매핑 → 파라미터 바인딩 → 응답 → Forward/Redirect
2. **Thymeleaf** `web02` `/ex02` — 출력·반복·조건 → 조각·레이아웃 → `th:field` 폼
3. **쿠키·세션 → 검증** `web03` `/ex03` — 쿠키·세션·세션 정보 → Bean Validation·검증 그룹·메시지
4. **JPA 기초** `web04` (콘솔) — 영속성 컨텍스트·1차 캐시·변경 감지·JPQL·준영속/merge·연관관계의 주인
5. **Spring Data JPA** `web05` `/ex05` — 리포지토리·쿼리 메소드·페이징·연관관계/N+1·DTO
6. **필터·인터셉터·예외** `web06` `/ex06` — 필터·인터셉터 로그 → 로그인 확인 비교 → 오류 페이지 → `@ExceptionHandler`·`@ControllerAdvice` → API 예외(JSON)
7. **파일·페이징** `web07` `/ex07` — 업로드/다운로드·페이징 계산
8. **REST API** `web08` `/ex08` — `@RestController` CRUD → `ResponseEntity` 상태 코드 → JSON 오류 → SSR vs `fetch`
9. **테스트** `test01junit` · `test02unitintegration` · `test03tdd` (`./gradlew test`) — JUnit·AssertJ·생명주기 → Fake·Mockito → `@DataJpaTest`·`@WebMvcTest`·`@SpringBootTest` → TDD·`@ParameterizedTest`·`@Nested`·`Clock` 주입
10. **Spring Security** `web10` `/ex10` — 의존성·`securityMatcher` → `SecurityFilterChain`(인가·폼 로그인·로그아웃) → `UserDetailsService`·BCrypt → `@AuthenticationPrincipal` → `hasRole`·`@PreAuthorize` → `sec:` 화면 → 로그인 실패 처리 → CSRF

> 순서 포인트: **요청·응답 다음에 Thymeleaf**, **검증·쿠키·세션 다음에 JPA**를 배치했다.
> 그래서 응답·Forward/Redirect·쿠키/세션 같은 화면은 문자열이 아니라 **타임리프 뷰**로 렌더링한다.
> (요청 매핑·파라미터·JSON·API 응답은 그대로 문자열/JSON으로 둔다.)

## 패키지 구성 (강의 순서대로)

| 패키지 | 파트 | 주요 내용 | 보완 예제 |
|---|---|---|---|
| `web00springmvc` | 스프링 기반과 DI | 컴포넌트 스캔·생성자 주입, `@Configuration`+`@Bean`, `@Primary`/`@Qualifier`, 등록된 빈 목록(`/ex00/beans`) | **스프링 없이 단위 테스트** (`BookControllerTest`) |
| `web01requestresponse` | 요청과 응답 | 요청 매핑, `@RequestParam`/`@ModelAttribute`/`@RequestBody`, 응답(뷰·JSON·`ResponseEntity`), Forward·Redirect 비교(`/ex01/forward`·`/ex01/redirect`)·PRG | **폼 바인딩**: 라디오·체크박스·다중 select → `List`/배열, `Map`/`MultiValueMap` (`/ex01/form.html`) |
| `web02thymeleaf` | Thymeleaf | 출력·변수·기본 객체·링크·리터럴/연산·반복·조건·주석/JS 인라인, 조각(fragment)·레이아웃 | **`th:field` 폼**: 라디오·체크박스·select 자동 선택 (`/ex02/form`) |
| `web03validationsession` | 쿠키와 세션 / 검증 | 쿠키·세션 화면, 세션 정보(`/ex03/session/info`), Bean Validation(`@Validated`/`BindingResult`/검증 그룹), `messages.properties` | **컬렉션·불린 검증**: `@NotEmpty`(다중선택)·`@AssertTrue`(약관 동의), **검증 그룹 비교**(`/ex03/groups/*`) |
| `web04jpabasics` | JPA 기초 | `EntityManager`·영속성 컨텍스트·1차 캐시·변경 감지·JPQL | **준영속(detach)·merge**, **연관관계의 주인**(Team/Player) |
| `web05springdatajpa` | Spring Data JPA | `JpaRepository`, 쿼리 메소드·`@Query`, 페이징(`Page`·`PagedModel`·페이지 블록 화면), 연관관계·N+1(fetch join·`@EntityGraph`·batch size), 변경 감지, DTO | **`Between`/`In`/복합 조건 검색**, **`Slice`(더보기)** (`/ex05/members/search`, `/ex05/members/list`) |
| `web06filterexception` | 필터·인터셉터·예외 | `Filter`, `HandlerInterceptor`, 오류 페이지(`error/4xx.html`), `@ExceptionHandler`·`@ControllerAdvice`(화면), `@RestControllerAdvice`(JSON) | **커스텀 예외 → 404** (`/ex06/api/members/{id}/strict`) |
| `web07filepaging` | 파일·페이징 | `MultipartFile` 업로드/다운로드, LIMIT/OFFSET 페이징 계산 | **다중 파일 업로드** `List<MultipartFile>` (`/ex07/upload`) |
| `web08rest` | REST API | `@RestController`, `@RequestBody`+`@Valid`(record DTO), `ResponseEntity`(201+Location/204), `@RestControllerAdvice(basePackages)` JSON 오류 | **SSR vs fetch 비교** (`/ex08/ssr`, `/ex08/todos.html`) |
| `test01junit` (main+test) | JUnit 기초 | `@Test`, Assertions·AssertJ, 생명주기, `@DisplayName` | `./gradlew test --tests "com.example.basic.test01junit.*"` |
| `test02unitintegration` (test) | 단위/통합 테스트 | Fake·Mockito, `@DataJpaTest`, `@WebMvcTest`+jsonPath, `@SpringBootTest`+MockMvc | 시큐리티가 있어 `@WebMvcTest`에 `@Import(Ex10SecurityConfig…)` |
| `test03tdd` (main+test) | 테스트 설계와 TDD | TDD로 만든 `PasswordValidator`, `@ParameterizedTest`(`@ValueSource`·`@CsvSource`·`@NullAndEmptySource`), `@Nested`+테스트 데이터 팩토리(`Cart`), `Clock` 주입(`OrderHours`) | `./gradlew test --tests "com.example.basic.test03tdd.*"` |
| `web10security` | Spring Security | `SecurityFilterChain`(`securityMatcher("/ex10/**")`), 폼 로그인·로그아웃, `UserDetailsService`·`UserDetails`·BCrypt, `@AuthenticationPrincipal`, `hasRole`·`@PreAuthorize`, `sec:` 네임스페이스, 실패 핸들러, CSRF(켜 둠) | **잠긴 계정 → `LockedException`** (`locked`/1234) |

## 알아 두기

- 여러 장이 한 앱에 모여 있어, **필터·인터셉터는 `/ex06/**` 로만** 좁게 걸어 다른 장에 영향을 주지 않게 했습니다.
  (실무 단일 애플리케이션에서는 교안처럼 `/*`, `/**` 전체에 거는 경우가 많습니다.)
- **N+1 문제**를 콘솔에서 직접 보려고 `default_batch_fetch_size`(해결책 ③)는 기본 **꺼 둔** 상태입니다.
  `application.yml` 의 해당 줄 주석을 풀면 N번 조회가 IN 쿼리 하나로 줄어드는 것을 확인할 수 있습니다.
- 타임리프 조각/레이아웃은 **Thymeleaf 3.1 권장 문법**(`~{...}` 로 감싼 조각 표현식)으로 작성했습니다.
  감싸지 않은 옛 문법(`th:replace="a :: b"`)과 `th:include` 는 폐기되었습니다.
- `templates/error/4xx.html`(web06 오류 페이지 실습)은 앱 전체에 적용됩니다. 다른 장에서 브라우저로 4xx 오류가 나도 이 화면이 보입니다.
- 시큐리티는 **`/ex10/**` 에만** 겁니다(`Ex10SecurityConfig`의 `securityMatcher`). 다른 장은 시큐리티 필터를 거치지 않습니다. 단 `@WebMvcTest`로 다른 장의 컨트롤러를 테스트하면 부트 기본 보안이 걸려 401이 나므로 `@Import({Ex10SecurityConfig.class, Ex10AuthFailureHandler.class})`를 붙입니다.
