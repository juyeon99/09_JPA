package com.ohgiraffers.section03;

import com.ohgiraffers.section02.crud.Menu;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class A_EntityLifeCycleTests {
    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;

    @BeforeAll
    public static void initFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
    }

    @BeforeEach
    public void initManager() {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterAll
    public static void closeFactory() {
        entityManagerFactory.close();
    }

    @AfterEach
    public void closeManager() {
        entityManager.close();
    }

    /*
    * 영속성 컨텍스트
    * 엔티티 매니저가 엔티티 객체를 저장하는 공간으로 엔티티 객체를 보관하고 관리한다.
    * 엔티티 매니저가 생성될 때 하나의 영속성 컨텍스트가 만들어진다.
    *
    * 엔티티 생명주기
    * 비영속: DB에 저장되지 않은 상태, EntityManager가 관리하지 X
    * 영속: 영속상태의 엔티티는 영속성 컨텍스트에 의해 관리되고, DB와 동기화
    * 준영속: 영속성 컨텍스트에 의해 더이상 관리되지 않지만, DB에 존재
    * 삭제상태: 영속성 컨텍스트와 DB에서 삭제된 상태
    * */

    @DisplayName("비영속 테스트")
    @Test
    public void test1() {
        // given
        Menu foundMenu = entityManager.find(Menu.class, 1);

        Menu newMenu = new Menu();
        newMenu.setMenuCode(foundMenu.getMenuCode());
        newMenu.setMenuName(foundMenu.getMenuName());
        newMenu.setMenuPrice(foundMenu.getMenuPrice());
        newMenu.setCategoryCode(foundMenu.getCategoryCode());
        newMenu.setOrderableStatus(foundMenu.getOrderableStatus());

        // when


        // then
        // 영속성 컨텍스트는 관리되고 있는 내용이 동일한 내용이라면, 주소값이 같아야한다.
        Assertions.assertFalse(foundMenu == newMenu);
    }

    @DisplayName("영속성 연속 조회 테스트")
    @Test
    public void test2() {
        /*
        * 엔티티 매니저가 영속성 컨텍스트에 엔티티 객체를 저장(persist)하면
        * 영속성 컨텍스트가 엔티티 객체를 관리하게 되고 이를 영속 상태라고 한다.
        * find(), JPQL을 사용한 조회도 영속상태가 된다.
        * */
        // given
        Menu foundMenu1 = entityManager.find(Menu.class, 11);
        Menu foundMenu2 = entityManager.find(Menu.class, 11);

        // when

        // then
        assertTrue(foundMenu1 == foundMenu2);   // 쿼리문이 하나만 나옴 => Persistence에 이미 있으므로
    }

    @DisplayName("영속성 객체 추가 테스트")
    @Test
    public void test3() {
        // given
        Menu newMenu = new Menu();
        newMenu.setMenuCode(50);
        newMenu.setMenuName("수박전");
        newMenu.setMenuPrice(50000);
        newMenu.setCategoryCode(1);
        newMenu.setOrderableStatus("Y");

        // when
        entityManager.persist(newMenu);

        Menu foundMenu = entityManager.find(Menu.class, 50);
        // 트랜젝션이 적용되지 않았기 때문에 쿼리문 발생 X

        // then
        assertTrue(newMenu == foundMenu);
    }

    @DisplayName("영속성 객체 추가 값 변경 테스트")
    @Test
    public void test4() {
        // given
        Menu newMenu = new Menu();
        newMenu.setMenuCode(50);
        newMenu.setMenuName("수박전");
        newMenu.setMenuPrice(50000);
        newMenu.setCategoryCode(1);
        newMenu.setOrderableStatus("Y");

        // when
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try{
            entityManager.persist(newMenu);
            newMenu.setMenuName("키위전");
            transaction.commit();
        } catch (Exception e){
            transaction.rollback();
            throw new RuntimeException();
        }
        Menu foundMenu = entityManager.find(Menu.class, 50);

        // then
        System.out.println("foundMenu.getMenuName() = " + foundMenu.getMenuName());
        assertEquals("키위전", foundMenu.getMenuName());
    }

    @DisplayName("준영속 detach 테스트")
    @Test
    public void test5() {
        // given
        Menu foundMenu1 = entityManager.find(Menu.class, 11);
        Menu foundMenu2 = entityManager.find(Menu.class, 12);

        // when
        entityManager.detach(foundMenu2);   // 특정 엔티티만 준영속 상태로 만듦 => DB에 적용되지 X

        foundMenu1.setMenuPrice(40000);
        foundMenu2.setMenuPrice(40000);

        // then
        assertEquals(40000,entityManager.find(Menu.class,11).getMenuPrice());
        assertEquals(40000,entityManager.find(Menu.class,12).getMenuPrice());   // Fail
    }

    @DisplayName("준영속 clear 테스트")
    @Test
    public void test6() {
        // given
        Menu foundMenu1 = entityManager.find(Menu.class, 11);
        System.out.println("foundMenu1 ====> " + foundMenu1);

        Menu foundMenu2 = entityManager.find(Menu.class, 12);

        // when
        entityManager.clear();  // 영속성 컨텍스트를 초기화
        foundMenu1.setMenuPrice(40000);
        System.out.println("foundMenu1 ====> " + foundMenu1);

        foundMenu2.setMenuPrice(40000);

        // then
        assertEquals(40000, entityManager.find(Menu.class,11).getMenuPrice());  // => price가 DB에 적용 X
        assertEquals(40000, entityManager.find(Menu.class,12).getMenuPrice());  // => price가 DB에 적용 X
    }

    @DisplayName("close 테스트")
    @Test
    public void test7() {
        // given
        Menu foundMenu1 = entityManager.find(Menu.class, 11);
        Menu foundMenu2 = entityManager.find(Menu.class, 12);

        // when
        entityManager.close();  // close는 영속성 컨텍스트를 종료

        foundMenu1.setMenuPrice(40000);
        foundMenu2.setMenuPrice(40000);

        // then
        assertEquals(40000,entityManager.find(Menu.class,11).getMenuPrice());   // entityManager가 close 되었기 때문에 확인 자체가 불가
        assertEquals(40000,entityManager.find(Menu.class,12).getMenuPrice());
    }

    @DisplayName("삭제 remove 테스트")
    @Test
    public void test8() {
        // given
        Menu foundMenu1 = entityManager.find(Menu.class, 50);

        // when
        entityManager.remove(foundMenu1);
        Menu refoundMenu = entityManager.find(Menu.class, 50);

        // then
        assertEquals(null, refoundMenu);
    }
}
