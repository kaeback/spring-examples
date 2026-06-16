package com.example.basic.web04jpabasics;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * [교안 JPA 기초 2~6] 영속성 컨텍스트의 핵심 동작을 EntityManager 로 직접 확인한다.
 *
 * <p>전체가 하나의 @Transactional 메서드다. 트랜잭션 '안'에서만 엔티티가 영속 상태가 되어
 * 1차 캐시·변경 감지 같은 혜택이 적용된다는 점이 이 실습의 핵심이다.
 *
 * <p>실행하면 콘솔에서 다음을 확인할 수 있다.
 *   - persist 직후엔 INSERT 가 안 나가고, flush 시점에 모여서 나간다(쓰기 지연)
 *   - 같은 id 를 두 번 find 해도 SELECT 는 한 번뿐(1차 캐시) → 두 객체가 == (동일성 보장)
 *   - setter 만 호출하고 save 류를 부르지 않아도 커밋 시 UPDATE 가 나간다(변경 감지)
 */
@Slf4j
@Service
public class JpaBasicsDemo {

    @PersistenceContext     // 스프링이 트랜잭션에 묶인 EntityManager 를 주입
    private EntityManager em;

    @Transactional
    public void run() {
        log.info("── 1) 비영속 → 영속, 그리고 쓰기 지연 ──");
        Item itemA = new Item("키보드", 30000);   // 비영속: 그냥 자바 객체
        Item itemB = new Item("마우스", 15000);
        em.persist(itemA);   // 영속: 영속성 컨텍스트가 관리 시작 (INSERT 는 아직 X, 쓰기 지연 저장소에 보관)
        em.persist(itemB);
        log.info("persist 호출 완료 — 아직 INSERT 안 나감(쓰기 지연). 곧 flush 합니다.");
        em.flush();          // 쌓아 둔 INSERT 를 한꺼번에 DB 로 (보통은 커밋 시 자동으로 일어남)

        log.info("── 2) 1차 캐시 + 동일성 보장 ──");
        Item find1 = em.find(Item.class, itemA.getId());   // 1차 캐시에 이미 있어 SELECT 안 나감
        Item find2 = em.find(Item.class, itemA.getId());
        log.info("동일성(find1 == find2) = {}", find1 == find2);   // true

        log.info("── 3) 변경 감지 (Dirty Checking) ──");
        find1.setPrice(35000);   // 값만 변경. em.update() 같은 건 없다(존재하지도 않음)
        log.info("price 만 바꿨습니다. 이 메서드가 끝나 커밋될 때 UPDATE 가 자동으로 나갑니다(콘솔 SQL 확인).");

        log.info("── 4) JPQL (엔티티 대상 쿼리) ──");
        // FROM 뒤가 테이블명(item)이 아니라 엔티티명(Item)이다.
        // JPQL 실행 직전 flush 가 일어나 위 변경(35000)이 반영된 상태로 조회된다.
        List<Item> result = em.createQuery(
                        "select i from Item i where i.price >= :p", Item.class)
                .setParameter("p", 20000)
                .getResultList();
        log.info("JPQL 결과(가격 20000 이상) = {}건", result.size());
        result.forEach(i -> log.info("   - {} ({}원)", i.getName(), i.getPrice()));
    }

    /**
     * [교안 3] 준영속(detached)과 merge — '영속 상태에서만' 변경 감지가 동작함을 확인한다.
     *
     * <p>detach 로 영속성 컨텍스트에서 분리하면 더는 관리되지 않아, setter 를 호출해도 UPDATE 가 안 나간다.
     * 다시 반영하려면 merge 로 '병합'해 영속 상태로 되돌려야 한다.
     */
    @Transactional
    public void demoDetachMerge() {
        Item item = new Item("모니터", 200000);
        em.persist(item);          // 영속
        em.flush();                // INSERT

        em.detach(item);           // 준영속: 영속성 컨텍스트에서 분리
        item.setPrice(150000);     // 준영속 상태라 '변경 감지 대상이 아님'
        log.info("detach 후 setPrice(150000) — 준영속이라 이 변경만으로는 DB에 반영되지 않습니다.");

        Item merged = em.merge(item);   // 병합: id 로 찾아 상태를 복사한 '영속' 엔티티를 반환
        log.info("merge 반환 객체 가격 = {} (이제 영속 상태 → 커밋 시 UPDATE 반영)", merged.getPrice());
        log.info("merge 가 돌려준 객체는 원본(item)과 다른 인스턴스다: (item == merged) = {}", item == merged);
    }
}
