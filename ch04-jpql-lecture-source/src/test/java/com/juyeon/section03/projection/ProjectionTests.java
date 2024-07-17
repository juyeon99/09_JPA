package com.juyeon.section03.projection;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProjectionTests {
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
    * 프로젝션
    * SELECT 절에 조회할 대상을 지정하는 것
    * (SELECT {프로젝션 대상} FROM ...)
    *
    * 1. 엔티티 프로젝션
    *   - 원하는 객체를 바로 조회 가능
    *   - 조회된 엔티티는 영속성 컨텍스트가 관리
    *
    * 2. 임베디드 타입 프로젝션
    *   - 엔티티와 거의 비슷하게 사용 (조회의 시작점이 될 수 X => FROM절에 사용 불가)
    *   - 영속성 컨텍스트에서 관리 X
    *
    * 3. 스칼라 타입 프로젝션
    *   - 숫자, 문자, 날짜 같은 기본 데이터 타입
    *   - 영속성 컨텍스트에서 관리 X
    *
    * 4. new 명령어를 활용한 프로젝션
    *   - 다양한 종류의 단순 값들을 DTO로 조회하는 방식 (new 패키지명.DTO명)
    *       => new 사용해서 만들기 때문에 def-constructor 필요
    *   - new 명령어를 사용한 클래스는 엔티티가 아니므로 영속성 컨텍스트에서 관리 X
    * */

    @Test
    @DisplayName("단일 엔티티 프로젝션 테스트")
    public void test1(){
        // given
        // when
        String jpql = "SELECT m FROM menu_section03 m";
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class).getResultList();

        // then
        assertNotNull(menuList);
        menuList.forEach(System.out::println);

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try{
            menuList.get(15).setMenuName("신메뉴");
            transaction.commit();
        } catch (Exception e){
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("양방향 연관관계 엔티티 프로젝션 테스트")
    public void test2(){
        // given
        int menuCodeParam = 3;

        // when
        String jpql = """
                        SELECT m.category 
                        FROM bidirection_menu m 
                        WHERE m.menuCode = :menuCode
                      """;

        BidirectionCategory category = entityManager.createQuery(jpql, BidirectionCategory.class)
                .setParameter("menuCode", menuCodeParam)
                .getSingleResult();

        // then
        assertNotNull(category);
        System.out.println("category = " + category);

        assertNotNull(category.getMenuList());
        category.getMenuList().forEach(System.out::println);
    }

    @Test
    @DisplayName("임베디드 타입 프로젝션 테스트")
    void test3(){
        // given
        // when
        String jpql = "SELECT m.menuInfo FROM embedded_menu m";
        List<MenuInfo> menuInfoList = entityManager.createQuery(jpql, MenuInfo.class).getResultList();

        // then
        assertNotNull(menuInfoList);
        menuInfoList.forEach(System.out::println);
    }

    @Test
    @DisplayName("TypedQuery를 이용한 스칼라(단일값) 타입 프로젝션 테스트")
    void test4(){
        // given
        // when
        String jpql = "SELECT c.categoryName FROM category_section03 c";
        List<String> categoryNameList = entityManager.createQuery(jpql, String.class).getResultList();

        // then
        assertNotNull(categoryNameList);
        categoryNameList.forEach(System.out::println);
    }

    @Test
    @DisplayName("Query를 이용한 스칼라(단일값) 타입 프로젝션 테스트")
    void test5(){
        // given
        // when
        String jpql = "SELECT c.categoryName, c.categoryCode FROM category_section03 c";
        List<Object[]> categoryList = entityManager.createQuery(jpql).getResultList();

        // then
        assertNotNull(categoryList);
        categoryList.forEach(
                row -> {
                    Arrays.stream(row).forEach(System.out::println);
                }
        );
    }

    @Test
    @DisplayName("new 명령어를 이용한 프로젝션 테스트")
    void test6(){
        // given
        // when
        String jpql = """
                        SELECT new com.juyeon.section03.projection.CategoryInfo(c.categoryCode, c.categoryName)
                        FROM category_section03 c
                      """;
        List<CategoryInfo> categoryInfoList = entityManager.createQuery(jpql, CategoryInfo.class).getResultList();

        // then
        assertNotNull(categoryInfoList);
        categoryInfoList.forEach(System.out::println);
    }
}
