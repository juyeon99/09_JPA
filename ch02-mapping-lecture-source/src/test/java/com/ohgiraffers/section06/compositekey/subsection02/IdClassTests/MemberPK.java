package com.ohgiraffers.section06.compositekey.subsection02.IdClassTests;

import lombok.*;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MemberPK {
    private int memberNo;
    private String memberId;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MemberPK memberPK = (MemberPK) obj;
        return memberNo == memberPK.memberNo && Objects.equals(memberId, memberPK.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberNo, memberId);    // memberNo와 memberId를 가지고 있는 hashCode 생성
    }
}
