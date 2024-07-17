package com.juyeon.section03.projection;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "embedded_menu")
@Table(name = "tbl_menu")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class EmbeddedMenu {
    @Id
    @Column(name = "menu_code")
    private int menuCode;

    @Embedded
    private MenuInfo menuInfo;

    @Column(name = "category_code")
    private int categoryCode;

    @Column(name = "orderable_status")
    private String orderableStatus;
}
