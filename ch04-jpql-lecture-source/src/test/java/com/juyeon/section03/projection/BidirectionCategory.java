package com.juyeon.section03.projection;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "bidirection_category")
@Table(name = "tbl_category")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BidirectionCategory {
    @Id
    @Column(name = "category_code")
    private int categoryCode;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "ref_category_code")
    private Integer refCategoryCode;

    @OneToMany(mappedBy = "category")
//    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    private List<BidirectionMenu> menuList;

    // 순환참조 방지
    @Override
    public String toString() {
        return "BidirectionCategory{" +
                "categoryCode=" + categoryCode +
                ", categoryName='" + categoryName + '\'' +
                ", refCategoryCode=" + refCategoryCode +
                '}';
    }
}
