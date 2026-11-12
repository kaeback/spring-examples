package com.example.basic.web07filepaging;

/**
 * [교안 2.3] 업로드 파일의 두 이름을 함께 보관하는 값 객체.
 *
 * <p>핵심: '사용자가 올린 원본 파일명'과 '서버에 실제로 저장한 파일명'을 분리한다.
 *  - originalName : 화면 표시·다운로드 시 사용자에게 보여 줄 이름 (예: 사진.png)
 *  - storeName    : 서버 디스크에 저장된 고유 이름 (예: a1b2c3...-e9.png) → 이름 충돌·경로 조작 방지
 *
 * <p>실무에서는 이 두 이름을 DB 에 저장하지만, 여기서는 메모리(FileStore)에 보관한다.
 */
public record UploadFile(String originalName, String storeName) {
}
