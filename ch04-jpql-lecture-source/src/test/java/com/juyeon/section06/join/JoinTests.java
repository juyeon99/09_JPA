package com.juyeon.section06.join;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JoinTests {
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
    * 조인의 종류
    *
    * 1. 일반 조인: SQL 조인들을 의미
    * 2. fetch 조인: JPQL에서 성능 최적화를 위해 제공하는 기능
    *               연관된 엔티티나 컬렉션을 한 번에 조회 가능
    *               지연 로딩이 아닌 즉시로딩을 수행 (JOIN FETCH)
    * */

    @Test
    @DisplayName("내부 조인을 이용한 조회 테스트")
    public void test1(){
        // given
        // when
        // 조인 속성에 대해 반복해서 SELECT (여기에서는 기본1번 + 조인8번 = 9번 실행) => 성능 저하(= N+1 문제)
//        String jpql = "SELECT m FROM menu_section06 m JOIN m.category c";       // 하이버네이트가 쿼리를 9번 실행 => 성능 저하
        String jpql = "SELECT m FROM menu_section06 m JOIN FETCH m.category c"; // JOIN FETCH를 이용하면 한번만 실행 => 성능 향상
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class).getResultList();

        // then
        assertNotNull(menuList);
        menuList.forEach(System.out::println);
    }

    @Test
    @DisplayName("외부조인을 위한 조회 테스트")
    void test2(){
        // given
        // when
        String jpql = """
                        SELECT m.menuName, c.categoryName
                        FROM menu_section06 m
                        RIGHT JOIN m.category c
                        ORDER BY m.category.categoryCode
                      """;
        List<Object[]> menuList = entityManager.createQuery(jpql, Object[].class).getResultList();

        // then
        assertNotNull(menuList);

//        for (Object[] row : menuList) {
//            for (Object col : row){
//                System.out.print(col + " ");
//            }
//            System.out.println();
//        }
        menuList.forEach(row -> {
            Stream.of(row).forEach(col -> System.out.print(col + " "));
            System.out.println();
        });
    }

    @Test
    @DisplayName("컬렉션 조인을 이용한 조회 테스트")
    void test3(){
        // 컬렉션 조인: 컬렉션을 지니고 있는 엔티티를 기준으로 조인하는 것을 의미

        // given
        // when
        String jpql = """
                        SELECT c.categoryName, m.menuName
                        FROM category_section06 c
                        LEFT JOIN c.menuList m
                      """;
        List<Object[]> categoryList = entityManager.createQuery(jpql, Object[].class).getResultList();

        // then
        assertNotNull(categoryList);
        categoryList.forEach(row -> {
            Stream.of(row).forEach(col -> System.out.print(col + " "));
            System.out.println();
        });
    }
}
