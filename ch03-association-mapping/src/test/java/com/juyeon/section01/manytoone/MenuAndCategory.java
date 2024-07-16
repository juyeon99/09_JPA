package com.juyeon.section01.manytoone;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "menu_and_category")
@Table(name = "tbl_menu")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MenuAndCategory {
    @Id
    @Column(name = "menu_code")
    private int menuCode;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "menu_price")
    private int menuPrice;

    @JoinColumn(name = "category_code")
    @ManyToOne(cascade = CascadeType.PERSIST)   // menu객체를 가져올 때 category도 함께 영속화시켜서 같이 가져옴
    private Category category;

    @Column(name = "orderable_status")
    private String orderableStatus;

    /*
    * @JoinColumn
    * - 외래키를 매핑하기 위해 사용되는 어노테이션
    *
    * @JoinColumn에서 사용할 수 있는 속성
    * - 'name': 참조하는 테이블의 컬럼명 지정
    * - 'referencedColumnName': 참조되는 테이블의 컬럼명 지정
    * - 'nullable': 참조하는 테이블의 컬럼에 null 값을 허용할지 지정
    * - 'unique': 참조하는 테이블의 컬럼에 유일성 제약조건을 추가할지 지정
    * - 'insertable': 새로운 엔티티가 저장될 때, 이 참조 컬럼이 SQL INSERT에 포함될지 지정
    * - 'updatable': 엔티티가 업데이트 될 때, 이 참조 컬럼이 SQL UPDATE에 포함될지 지정
    * - 'columnDefinition': 이 참조 컬럼에 대한 SQL DDL을 직접 지정
    * - 'table': 참조하는 테이블의 이름 지정
    * - 'foreignKey': 참조하는 테이블에 생성될 외래 키에 대한 추가 정보 지정
    * */

    /*
    * @ManyToOne
    * - ManyToOne 연관관계에서 사용되는 어노테이션
    *
    * @ManyToOne에서 사용할 수 있는 속성
    * - 'cascade': 연관된 엔티티에 대한 영속성 전이를 설정
    * - 'fetch': 연관된 엔티티를 로딩하는 전략 설정
    * - 'optional': 연관된 엔티티가 필수인지 선택인지를 설정
    * */
}
