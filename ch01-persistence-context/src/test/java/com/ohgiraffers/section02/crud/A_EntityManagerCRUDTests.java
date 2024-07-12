package com.ohgiraffers.section02.crud;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

public class A_EntityManagerCRUDTests {
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

    @DisplayName("메뉴 코드로 메뉴 조회 테스트")
    @Test
    public void selectMenuByMenuCode(){
        // given
        int menuCode = 2;

        // when
        Menu menu = entityManager.find(Menu.class /* Menu 타입의 객체를 가져옴 */, menuCode /* menuCode를 이용하여 찾아옴 */);

        // then
        Assertions.assertNotNull(menu);

        Assertions.assertEquals(menuCode, menu.getMenuCode());
        System.out.println("menu = " + menu);

        Menu menu2 = entityManager.find(Menu.class, menuCode);  // 이미 Persistence가 가지고 있는 객체이므로 DB까지 가지 않음
        System.out.println("menu2 = " + menu2);

        Menu menu3 = entityManager.find(Menu.class, 3);  // Persistence가 가지고 있지 않은 객체이므로 DB에 접근
        System.out.println("menu3 = " + menu3);
    }

    @DisplayName("새로운 메뉴 추가 테스트")
    @Test
    public void insertNewMenuTest(){
        // given
        Menu menu = new Menu();
        menu.setMenuName("JPA 테스트용 신규 메뉴");
        menu.setMenuPrice(50000);
        menu.setCategoryCode(4);
        menu.setOrderableStatus("Y");

        // when
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();    // 트랜젝션 시작 선언
        try{
            entityManager.persist(menu);    // 영속성 컨텍스트에 등록
            transaction.commit();
        } catch (Exception e){
            transaction.rollback();
            e.printStackTrace();
        }

        // then
        Assertions.assertTrue(entityManager.contains(menu));
    }

    @DisplayName("메뉴 이름 수정 테스트")
    @Test
    public void modifyMenuNameTest(){
        // given
        Menu menu = entityManager.find(Menu.class, 23);
        System.out.println("menu = " + menu);

        String newMenuName = "생갈치스무디";

        // when
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try{
            menu.setMenuName(newMenuName);
            transaction.commit();
        } catch (Exception e){
            transaction.rollback();
            e.printStackTrace();
        }

        // then
        Assertions.assertEquals(newMenuName, menu.getMenuName());
        Assertions.assertEquals(newMenuName, entityManager.find(Menu.class, 23).getMenuName()); // DB에 보내지 않고 이미 Persistence에 있기 때문에 확인 가능
    }

    @DisplayName("메뉴 삭제하기 테스트")
    @Test
    public void deleteMenu(){
        // given
        Menu menuToRemove = entityManager.find(Menu.class, 23);
        System.out.println("menuToRemove = " + menuToRemove);

        // when
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            entityManager.remove(menuToRemove);
            transaction.commit();
        } catch (Exception e){
            transaction.rollback();
            e.printStackTrace();
        }

        // then
        Menu removedMenu = entityManager.find(Menu.class, 23);
    }
}
