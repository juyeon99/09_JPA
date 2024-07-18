package com.juyeon.springdatajpa.menu.model.repository;

import com.juyeon.springdatajpa.menu.model.entity.Menu;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// EntityManagerFactory, EntityManager, EntityTransaction -> 자동 구현
// JpaRepository<엔티티명, PK타입>
public interface MenuRepository extends JpaRepository<Menu, Integer /* => PK값이 Integer */>{
    // 쿼리 메소드 (~GreaterThan, ~GreaterThanOrderBy~Desc)
    // menuPrice보다 큰 금액의 메뉴 목록 조회
    List<Menu> findByMenuPriceGreaterThan(Integer menuPrice, Sort sortMenuPrice);
    List<Menu> findByMenuPriceGreaterThanOrderByMenuPriceDesc(Integer menuPrice);

    // menuPrice와 같은 금액의 메뉴 목록 조회
    List<Menu> findByMenuPriceEquals(Integer menuPrice);
}