package com.juyeon.section02.onetomany;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "category_and_menu")
@Table(name = "tbl_category")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class CategoryAndMenu {
    /*
    * fetch
    *
    * - @OneToMany(fetch = FetchType.LAZY)  => 지연 로딩이 default
    * - @ManyToMany                         => 지연 로딩이 default
    * - @ManyToOne(fetch = FetchType.EAGER) => 이른 로딩이 default
    * - @OneToOne                           => 이른 로딩이 default
    * */

    @Id
    @Column(name = "category_code")
    private int categoryCode;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "ref_category_code")
//    private int refCategoryCode;
    private Integer refCategoryCode;

    @JoinColumn(name = "category_code")
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Menu> menuList;
}
