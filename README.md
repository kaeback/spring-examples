# spring-basic — 웹 개발 실습

자바 백엔드 강의 「웹 개발」 챕터(**요청과 응답 ~ Spring Data JPA**)의 수업용 실습 프로젝트입니다.
각 파트를 **패키지별로 분리**하고, 간단한 예제와 상세한 한글 주석으로 설명했습니다.

- Spring Boot 3.5.x / Java 21 / Gradle
- DB: **H2 인메모리**(별도 설치 불필요) · 화면: **Thymeleaf** · 검증: **Bean Validation**

## 실행

```bash
./gradlew bootRun
```

- 브라우저 허브: <http://localhost:8080/>
- H2 콘솔: <http://localhost:8080/h2-console> (JDBC URL: `jdbc:h2:mem:testdb`, 사용자 `sa`)
- **web04 / web05(JPA) 데모는 앱이 켜질 때 콘솔에 자동 출력**됩니다. 함께 찍히는 SQL 로그를 관찰하세요.

## 실습 진행 순서

아래 번호 순서대로 진행한다. 패키지 번호 = URL 번호(`webNN` ↔ `/exNN`)로 맞춰 두었다.

1. **요청과 응답** `web01` `/ex01` — 요청 매핑 → 파라미터 바인딩 → 응답 → Forward/Redirect
2. **Thymeleaf** `web02` `/ex02` — 출력·반복·조건 → 조각·레이아웃 → `th:field` 폼
3. **검증과 쿠키·세션** `web03` `/ex03` — Bean Validation → 쿠키·세션
4. **JPA 기초** `web04` (콘솔) — 영속성 컨텍스트·1차 캐시·변경 감지·준영속/merge
5. **Spring Data JPA** `web05` `/ex05` — 리포지토리·쿼리 메소드·페이징·연관관계/N+1·DTO
6. **필터·인터셉터·예외** `web06` `/ex06` — 공통 처리·예외 변환
7. **파일·페이징** `web07` `/ex07` — 업로드/다운로드·페이징 계산

> 순서 포인트: **요청·응답 다음에 Thymeleaf**, **검증·쿠키·세션 다음에 JPA**를 배치했다.
> 그래서 응답·Forward/Redirect·쿠키/세션 같은 화면은 문자열이 아니라 **타임리프 뷰**로 렌더링한다.
> (요청 매핑·파라미터·JSON·API 응답은 그대로 문자열/JSON으로 둔다.)

## 패키지 구성 (강의 순서대로)

| 패키지 | 파트 | 주요 내용 | 보완 예제 |
|---|---|---|---|
| `web01requestresponse` | 요청과 응답 | 요청 매핑, `@RequestParam`/`@ModelAttribute`/`@RequestBody`, 응답(뷰·JSON), Forward·Redirect·PRG | **폼 바인딩**: 라디오·체크박스·다중 select → `List`/배열, `Map`/`MultiValueMap` (`/ex01/form.html`) |
| `web02thymeleaf` | Thymeleaf | 출력·변수·반복·조건, 조각(fragment)·레이아웃 | **`th:field` 폼**: 라디오·체크박스·select 자동 선택 (`/ex02/form`) |
| `web03validationsession` | 검증과 쿠키·세션 | Bean Validation(`@Valid`/`BindingResult`/검증 그룹), 쿠키·세션 화면 | **컬렉션·불린 검증**: `@Size`(다중선택 최소 1개)·`@AssertTrue`(약관 동의) |
| `web04jpabasics` | JPA 기초 | `EntityManager`·영속성 컨텍스트·1차 캐시·변경 감지·JPQL | **준영속(detach)·merge** |
| `web05springdatajpa` | Spring Data JPA | `JpaRepository`, 쿼리 메소드·`@Query`, 페이징, 연관관계·N+1, DTO | **`Between`/`In`/복합 조건 검색**, **`Slice`(더보기)** (`/ex05/members/search`) |
| `web06filterexception` | 필터·인터셉터·예외 | `Filter`, `HandlerInterceptor`, `@RestControllerAdvice`+`@ExceptionHandler` | **커스텀 예외 → 404** (`/ex06/api/members/{id}/strict`) |
| `web07filepaging` | 파일·페이징 | `MultipartFile` 업로드/다운로드, LIMIT/OFFSET 페이징 계산 | **다중 파일 업로드** `List<MultipartFile>` (`/ex07/upload`) |

## 알아 두기

- 여러 장이 한 앱에 모여 있어, **필터·인터셉터는 `/ex06/**` 로만** 좁게 걸어 다른 장에 영향을 주지 않게 했습니다.
  (실무 단일 애플리케이션에서는 교안처럼 `/*`, `/**` 전체에 거는 경우가 많습니다.)
- **N+1 문제**를 콘솔에서 직접 보려고 `default_batch_fetch_size`(해결책 ③)는 기본 **꺼 둔** 상태입니다.
  `application.yml` 의 해당 줄 주석을 풀면 N번 조회가 IN 쿼리 하나로 줄어드는 것을 확인할 수 있습니다.
- 타임리프 조각/레이아웃은 **Thymeleaf 3.1 권장 문법**(`~{...}` 로 감싼 조각 표현식)으로 작성했습니다.
  감싸지 않은 옛 문법(`th:replace="a :: b"`)과 `th:include` 는 폐기되었습니다.
