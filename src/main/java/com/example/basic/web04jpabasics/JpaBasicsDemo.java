package com.example.basic.web04jpabasics;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * [교안 2.2~2.7] 영속성 컨텍스트의 핵심 동작을 EntityManager 로 직접 확인한다.
 *
 * <p>메서드마다 @Transactional 을 따로 붙였다. 메서드 하나 = 트랜잭션 하나 = 영속성 컨텍스트 하나라서,
 * 메서드가 끝나면(커밋) 영속성 컨텍스트도 사라진다. 콘솔에서 SQL 이 '언제' 나가는지 로그 순서로 확인하자.
 */
@Slf4j
@Service
public class JpaBasicsDemo {

    @PersistenceContext     // 스프링이 현재 트랜잭션에 묶인 EntityManager 를 주입한다
    private EntityManager em;

    /** [교안 2.2] 비영속 → 영속 → 삭제, 그리고 쓰기 지연. 키보드의 id 를 돌려준다. */
    @Transactional
    public Long lifecycle() {
        Item keyboard = new Item("키보드", 30000);   // 비영속: 그냥 자바 객체
        Item mouse = new Item("마우스", 15000);
        log.info("persist 전: 영속 상태인가? {}", em.contains(keyboard));   // false

        em.persist(keyboard);   // 영속: 영속성 컨텍스트가 관리 시작
        em.persist(mouse);      // IDENTITY 전략이라 id 를 받으려고 INSERT 가 바로 나간다
        log.info("persist 후: 영속 상태인가? {}, id = {}", em.contains(keyboard), keyboard.getId());

        em.remove(mouse);       // 삭제: DELETE 는 쓰기 지연 저장소에 쌓이기만 한다
        log.info("remove 호출 — 아직 DELETE 는 나가지 않았다. flush 합니다.");
        em.flush();             // 쌓인 SQL 을 DB 로 (보통은 커밋 직전에 자동으로 일어난다)
        log.info("flush 완료");
        return keyboard.getId();
    }

    /** [교안 2.3] 1차 캐시와 동일성 보장 — 같은 id 를 두 번 찾아도 SELECT 는 한 번. */
    @Transactional
    public void firstLevelCache(Long id) {
        log.info("첫 번째 find");
        Item find1 = em.find(Item.class, id);   // 1차 캐시에 없으니 DB 에서 SELECT → 캐시에 보관
        log.info("두 번째 find");
        Item find2 = em.find(Item.class, id);   // 1차 캐시에서 꺼낸다 (SELECT 없음)
        log.info("find1 == find2 : {}", find1 == find2);   // 같은 객체 → true
    }

    /** [교안 2.4] 변경 감지 — setter 만 호출하고 저장 메서드는 부르지 않는다. */
    @Transactional
    public void dirtyChecking(Long id) {
        Item item = em.find(Item.class, id);    // 영속 상태 + 스냅샷 보관
        item.setPrice(35000);                   // 값만 바꾼다. em.update() 같은 메서드는 없다
        log.info("가격을 35000 으로 바꿨다. 메서드가 끝나 커밋될 때 UPDATE 가 나간다.");
    }

    /** [교안 2.5] JPQL 과 자동 flush — JPQL 실행 직전에 쌓인 변경이 먼저 DB 로 나간다. */
    @Transactional
    public void jpql(Long id) {
        em.persist(new Item("모니터", 200000));
        Item keyboard = em.find(Item.class, id);
        keyboard.setPrice(40000);
        log.info("가격을 40000 으로 바꾸고 바로 JPQL 실행");

        // FROM 뒤가 테이블명(item)이 아니라 엔티티명(Item)이다
        List<Item> result = em.createQuery(
                        "select i from Item i where i.price >= :price order by i.price", Item.class)
                .setParameter("price", 20000)
                .getResultList();
        result.forEach(i -> log.info("JPQL 결과: {} {}원", i.getName(), i.getPrice()));
    }

    /** [교안 2.6] 준영속(detach)과 merge — 영속 상태에서만 변경 감지가 동작한다. */
    @Transactional
    public void detachAndMerge(Long id) {
        Item item = em.find(Item.class, id);
        em.detach(item);           // 준영속: 영속성 컨텍스트에서 분리
        item.setPrice(1000);       // 준영속이라 변경 감지 대상이 아니다
        log.info("detach 후 가격 변경: 영속 상태인가? {}", em.contains(item));   // false
        em.flush();
        log.info("flush 했지만 UPDATE 없음 (관리 대상이 아니니까)");

        Item merged = em.merge(item);   // 같은 id 의 영속 엔티티에 item 의 값을 복사해 돌려준다
        log.info("merge 결과: 영속 상태인가? {}, item == merged : {}", em.contains(merged), item == merged);
        // 메서드가 끝나 커밋될 때 merged 의 변경(1000원)이 UPDATE 된다
    }

    /** [교안 2.7] 연관관계의 주인 — FK 는 주인(Player.team) 쪽 값으로만 저장된다. */
    @Transactional
    public void owner() {
        Team team = new Team("레드");
        em.persist(team);

        Player minsu = new Player("민수");
        team.getPlayers().add(minsu);      // 주인이 아닌 쪽에만 넣었다 → FK 는 null
        em.persist(minsu);

        Player jihun = new Player("지훈");
        jihun.setTeam(team);               // 주인 쪽에 넣었다 → FK 저장
        team.getPlayers().add(jihun);      // (객체 상태를 맞추려고 반대쪽에도 추가한다)
        em.persist(jihun);

        em.flush();
        em.clear();                        // 1차 캐시를 비우고 DB 에서 다시 읽는다

        List<Player> players = em.createQuery("select p from Player p order by p.id", Player.class)
                .getResultList();
        for (Player p : players) {
            log.info("{} → team_id = {}", p.getName(), p.getTeam() == null ? null : p.getTeam().getId());
        }
    }
}
