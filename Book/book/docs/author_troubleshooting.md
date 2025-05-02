
# Author 중복 문제 트러블슈팅

## 📌 문제 개요
도서 등록 기능을 구현하던 중 `author` 테이블에 동일한 이름의 작가가 **중복으로 3개** 등록되는 현상이 발생하였음.

예시 데이터:
```
author_id | name   | country
----------|--------|---------
1         | 김영한 | 대한민국
2         | 김영한 | 대한민국
3         | 김영한 | 대한민국
```

## ⚠️ 문제 원인
- 도서 생성 시 작가 이름이 동일하더라도 매번 새로운 `Author` 엔티티를 생성하여 저장하고 있었음.
- 따라서 `Author` 중복 방지가 되지 않아 DB에 동일한 이름을 가진 작가가 중복 저장됨.

## 🛠️ 해결 절차

### 1. 중복 데이터 정리
중복된 작가 ID들을 단일 ID(예: 1번)로 통일한 후, 나머지 중복된 작가는 삭제.

```sql
-- Book 테이블에서 중복된 author_id를 1로 업데이트
UPDATE book
SET author_id = 1
WHERE author_id IN (2, 3);

-- 중복된 author 레코드 삭제
DELETE FROM author
WHERE name = '김영한' AND author_id != 1;
```

> ⚠️ 외래 키 제약조건(FK) 때문에 먼저 Book 테이블의 참조를 정리해야 함.

### 2. 애플리케이션 단 처리
`AuthorService`에 `findOrCreateAuthor(String name)` 메소드를 구현하여 동일한 이름의 작가가 이미 존재하면 재사용.

```java
public Author findOrCreateAuthor(String name) {
    return authorRepository.findByName(name)
            .orElseGet(() -> authorRepository.save(new Author(name, "대한민국")));
}
```

### 3. DB에 Unique 제약 조건 추가 (선택)
중복 방지를 위해 `name` 컬럼에 Unique 제약 조건 부여.

```sql
ALTER TABLE author ADD CONSTRAINT uc_author_name UNIQUE (name);
```

## ✅ 결론
- 중복 작가 문제는 도메인 설계 및 서비스 로직에서의 검증 부족으로 인해 발생.
- DB 정리 + 서비스 로직 수정 + Unique 제약 조건 추가로 근본 해결함.
