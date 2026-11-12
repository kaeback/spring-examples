package com.example.basic.web06filterexception;

/**
 * [교안 4.4 심화] 도메인(업무) 의미를 담은 커스텀 예외.
 *
 * <p>"회원을 못 찾음" 같은 업무 상황을 IllegalArgumentException 대신 전용 예외로 표현하면,
 * @ExceptionHandler 에서 상황별로 알맞은 상태코드(404)와 메시지로 변환하기 쉽다.
 *
 * <p>RuntimeException 을 상속하므로(언체크 예외) 트랜잭션 롤백 기본 대상이기도 하다.
 */
public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(String message) {
        super(message);
    }
}
