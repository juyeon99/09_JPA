package com.juyeon.section09.nativequery;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JpqlNativeQueryTests {
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
    * Native Query
    * - SQL 쿼리를 그대로 사용하는 것을 의미
    * - ORM의 기능을 이용하면서 SQL 쿼리도 활용할 수 있어서 더욱 강력한 DB 접근 가능
    * - 복잡한 쿼리, 특정 DB에서만 사용가능한 기능을 사용할 때 사용
    *
    * 네이티브 쿼리 API 종류 3가지
    * 1. 결과 타입 정의 (엔티티클래스만 지정 가능)
    *    public Query createNativeQuery(String sqlString, Class resultClass)
    * 2. 결과 타입을 정의할 수 없을 때
    *    public Query createNativeQuery(String sqlString)
    * 3. 결과 매핑
    *    public Query createNativeQuery(String sqlString, String resultSetMapping)
    * */

    @Test
    @DisplayName("결과타입을 지정해서 Native Query 사용하기")
    void test1(){
        // given
        Long menuCode = 15L;

        // when
        String jpql = """
                        SELECT *
                        FROM tbl_menu
                        WHERE menu_code = ?
                      """;
        Query query = entityManager.createNativeQuery(jpql, Menu.class)
                .setParameter(1, menuCode);
        Menu menu = (Menu) query.getSingleResult();
        System.out.println("menu = " + menu);

        Menu menu2 = entityManager.find(Menu.class, menuCode);  // SELECT 쿼리가 다시 동작하지 X => 영속성 컨텍스트에 이미 있음을 알 수 있음
        System.out.println("menu2 = " + menu2);

        // then
        assertNotNull(menu);
    }

    @Test
    @DisplayName("결과 타입을 지정하지 않고 Native Query 사용하기")
    void test2(){
        // given
        Long menuCode = 15L;

        // when
        String sql = """
                        SELECT menu_code, menu_name, (
                                            SELECT category_name
                                            FROM tbl_category
                                            WHERE category_code = m.category_code)
                        FROM tbl_menu m
                        WHERE menu_code = ?
                     """;
        Query query = entityManager.createNativeQuery(sql)
                .setParameter(1, menuCode);
        Object[] row = (Object[]) query.getSingleResult();

        for (Object o : row){
            System.out.println(o);
        }

        // then
        // row의 크기가 3이면 pass
        assertThat(row).hasSize(3);
    }

    @Test
    @DisplayName("@NamedNativeQuery 사용하기 - 엔티티")
    void test3(){
        // given
        Long menuCode = 15L;

        // when
        Query query = entityManager.createNamedQuery("section09.Menu.findByMenuCode", Menu.class)
                .setParameter(1, menuCode);
        Menu menu = (Menu) query.getSingleResult();
        System.out.println("menu = " + menu);

        // then
        assertNotNull(menu);
    }
}
