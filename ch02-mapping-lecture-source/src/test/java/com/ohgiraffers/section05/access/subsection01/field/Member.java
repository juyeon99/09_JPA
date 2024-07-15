package com.ohgiraffers.section05.access.subsection01.field;

import jakarta.persistence.*;
import lombok.*;

@Entity(name="member_section05_sub01")
@Table(name="tbl_member_section05_sub01")
@NoArgsConstructor
@AllArgsConstructor
/*
 * @Access()
 * 필드 접근이 기본값이므로 설정을 제거해도 동일하게 작동
 * 클래스, 필드에 다 적용 시키면 필드에 적용시킨 내용이 우선적으로 적용
 * */
//@Access(AccessType.FIELD)   // 모든 필드에 대해서 필드 직접 접근 방식 사용 => Getter 필요 X
@Access(AccessType.PROPERTY)    // 필드에 적용시킨 내용이 우선적으로 적용되므로 이 라인은 적용 안됨 // PROPERTY는 Getter가 있어야 사용 가능
//@Getter
@Setter
@ToString
public class Member {
    @Id
    @Column(name = "member_no")
    @Access(AccessType.FIELD)
    private int memberNo;

    @Column(name = "member_id")
    @Access(AccessType.FIELD)
    private String memberId;

    @Column(name = "member_pwd")
    @Access(AccessType.FIELD)
    private String memberPwd;

    @Column(name = "nickname")
    @Access(AccessType.FIELD)
    private String nickname;
}
