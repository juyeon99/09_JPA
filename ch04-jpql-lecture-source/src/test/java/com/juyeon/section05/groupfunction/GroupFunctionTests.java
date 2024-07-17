package com.juyeon.section05.groupfunction;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GroupFunctionTests {
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
    * 그룹함수
    * -> JPQL의 그룹 함수와 기존 SQL의 그룹함수는 차이가 거의 X
    * COUNT, SUM, AVG, MAX, MIN
    *
    * 주의사항
    * 1. 그룹함수의 반환 타입은 결과 값이 정수면 Long, 실수면 Double을 반환
    * 2. 값이 없는 상태에서 COUNT를 제외한 그룹함수는 null이 되고 COUNT는 0이 된다.
    *    => null로 받은 상태에서는 long/double 같은 기본자료형으로 받게되면 언박싱 과정에서 NPE 발생
    * 3. 그룹함수의 반환 자료형은 Long/Double형이기 때문에 Having절에서
    *    그룹함수 결과값과 비교하기 위한 파라미터 타입은 Long/Double(Wrapper type)로 해야한다.
    *
    * => 받을 때 Wrapper 타입 사용해야 함
    * */

    @Test
    @DisplayName("특정 카테고리에 등록된 메뉴 수 조회(COUNT)")
    public void test1(){
        // given
//        int cagetoryCodeParam = 4;
        int cagetoryCodeParam = 2;  // count가 0이어도 기본자료형(long)을 써도 핸들 가능 (null이 아니기 때문에)

        // when
        String jpql = """
                        SELECT COUNT(m.menuPrice)
                        FROM menu_section05 m
                        WHERE m.categoryCode = :categoryCode
                      """;
        long menuCount = entityManager.createQuery(jpql, Long.class)
                .setParameter("categoryCode", cagetoryCodeParam)
                .getSingleResult();

        // then
        assertTrue(menuCount >= 0);
        System.out.println("menuCount = " + menuCount);
    }

    @Test
    @DisplayName("COUNT를 제외한 다른 그룹함수의 조회결과가 없는 경우 테스트")
    public void test2(){
        // given
        int cagetoryCodeParam = 2;

        // when
        String jpql = """
                        SELECT SUM(m.menuPrice)
                        FROM menu_section05 m
                        WHERE m.categoryCode = :categoryCode
                      """;

//        Long/* long => NPE */ sum = entityManager.createQuery(jpql, Long.class)
//                .setParameter("categoryCode", cagetoryCodeParam)
//                .getSingleResult();

        // then
//        assertThrows(NullPointerException.class, () -> {
//            long sum = entityManager.createQuery(jpql, Long.class)
//                    .setParameter("categoryCode", cagetoryCodeParam)
//                    .getSingleResult();
//        });

        assertDoesNotThrow(() -> {
            Long sum = entityManager.createQuery(jpql, Long.class)
                    .setParameter("categoryCode", cagetoryCodeParam)
                    .getSingleResult();
        });
    }

    @Test
    @DisplayName("GROUP BY와 HAVING을 사용한 조회 테스트")
    void test3(){
        // given
//        int minPrice = 50000;
        long minPrice = 50000L;
        // 그룹함수의 반환 타입은 long이므로 비교를 위한 파라미터도 long타입 사용

        // when
        String jpql = """
                        SELECT m.categoryCode, SUM(m.menuPrice)
                        FROM menu_section05 m
                        GROUP BY m.categoryCode
                        HAVING SUM(m.menuPrice) >= :minPrice
                      """;

        List<Object[]> sumOfPriceByCategoryList = entityManager.createQuery(jpql, Object[].class)
                .setParameter("minPrice", minPrice)
                .getResultList();

        // then
        assertNotNull(sumOfPriceByCategoryList);

//        for (Object[] row : sumOfPriceByCategoryList) {
//            Arrays.stream(row).forEach(System.out::println);
//        }
        sumOfPriceByCategoryList.forEach(row -> {
            Arrays.stream(row).forEach(System.out::println);
        });
    }
}
