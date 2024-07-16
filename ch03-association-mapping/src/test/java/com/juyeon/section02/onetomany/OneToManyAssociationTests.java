package com.juyeon.section02.onetomany;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OneToManyAssociationTests {
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

    @Test
    @DisplayName("OneToMany 연관관계 객체 그래프 탐색을 이용한 조회 테스트")
    public void test1(){
        // given
        int categoryCode = 10;

        // when
        // OneToMany 연관관계의 경우 해당 테이블만 조회하고 연관된 메뉴 테이블은 아직 조회X
        CategoryAndMenu categoryAndMenu = entityManager.find(CategoryAndMenu.class, categoryCode);

        // then
        assertNotNull(categoryAndMenu);
        // (출력구문 작성 후,) 사용하는 경우 연관 테이블을 조회해오는 쿼리 동작
        System.out.println("categoryAndMenu = " + categoryAndMenu);
    }

    @Test
    @DisplayName("OneToMany 연관관계 객체 삽입 테스트")
    public void test2(){
        // given
        CategoryAndMenu categoryAndMenu = new CategoryAndMenu();
        categoryAndMenu.setCategoryCode(800);
        categoryAndMenu.setCategoryName("OneToMany 추가 카테고리2");
        categoryAndMenu.setRefCategoryCode(null);

        List<Menu> menuList = new ArrayList<>();
        Menu menu = new Menu();
        menu.setMenuCode(777);
        menu.setMenuName("일대다 아이스크림");
        menu.setMenuPrice(50000);
        menu.setOrderableStatus("Y");

        menu.setCategoryCode(categoryAndMenu.getCategoryCode());
        menuList.add(menu);
        categoryAndMenu.setMenuList(menuList);

        // when
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        entityManager.persist(categoryAndMenu);
        transaction.commit();

        // then
        CategoryAndMenu foundCategoryAndMenu = entityManager.find(CategoryAndMenu.class, 800);
        System.out.println("foundCategoryAndMenu = " + foundCategoryAndMenu);
    }
}
