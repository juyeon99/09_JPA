package com.ohgiraffers.section02.crud;

import jakarta.persistence.*;
import lombok.*;

@Entity(name="select02_menu")   // 엔티티 객체로 만들기 위한 어노테이션 (다른 패키지에 동일한 이름의 클래스가 존재하면 name 지정 필수)
@Table(name="tbl_menu")   // DB에 매핑될 테이블 이름 설정

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter // setter를 사용하지 않는걸 권장
@ToString
public class Menu {
    @Id // pk에 해당하는 속성에 지정
    @Column(name="menu_code")   // DB에 대응되는 컬럼명 지정
//    @GeneratedValue(strategy=GenerationType.IDENTITY)   // 기본키 값을 DB에서 생성하도록 지정
    private int menuCode;

    @Column(name="menu_name")
    private String menuName;

    @Column(name="menu_price")
    private int menuPrice;

    @Column(name="category_code")
    private int categoryCode;

    @Column(name="orderable_status")
    private String orderableStatus;
}
