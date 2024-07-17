package com.juyeon.section03.projection;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "bidirection_menu")
@Table(name = "tbl_menu")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BidirectionMenu {
    @Id
    @Column(name = "menu_code")
    private int menuCode;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "menu_price")
    private int menuPrice;

    @ManyToOne
    @JoinColumn(name="category_code")
    private BidirectionCategory category;

    @Column(name = "orderable_status")
    private String orderableStatus;
}
