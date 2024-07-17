package com.juyeon.section07.subquery;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SubQueryTests {
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

    // JPQL도 일반 SQL처럼 서브쿼리 지원
    // SELECT, FROM 절에서는 사용X
    // WHERE, HAVING 절에서만 사용 가능
    @Test
    @DisplayName("서브쿼리를 이용한 메뉴 조회 테스트")
    void test(){
        // given
        String categoryNameParam = "한식";

        // when
        // 서브쿼리: 한식 이름에 맞는 카테고리 찾기
        // 외부쿼리: 서브쿼리에서 조회된 카테고리에 맞는 메뉴들을 조회
        String jpql = """
                        SELECT m
                        FROM menu_section07 m
                        WHERE m.categoryCode = (SELECT c.categoryCode
                                                FROM category_section07 c
                                                WHERE c.categoryName = :categoryName)
                      """;
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class)
                .setParameter("categoryName", categoryNameParam)
                .getResultList();

        // then
        assertNotNull(menuList);
        menuList.forEach(System.out::println);
    }
}
