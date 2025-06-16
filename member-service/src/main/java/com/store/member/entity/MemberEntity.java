package com.store.member.entity;

import com.store.member.config.security.Role;
import com.store.member.domain.MemberSignUpDomain;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Table(name = "MEMBER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@Entity
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column
    private String email;

    @Column
    private String name;

    @Column
    private String password;

    @Column
    private String phone;

    @Column
    private String approvalYn;

    @Enumerated(EnumType.STRING)
    private Role role;

    @CreatedDate
    private Date created;

    @LastModifiedDate
    private Date modified;

    @Column
    private Date approvalDate;

    @OneToMany(mappedBy = "member" , fetch = FetchType.LAZY)
    private List<ShippingEntity> shipping = new ArrayList<>();

    private MemberEntity(String email, String name, String password, String phone, String approvalYn, Role role) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.approvalYn = approvalYn;
        this.role = role;
    }

    // 생성 메서드
    public static MemberEntity signUp(MemberSignUpDomain domain, String encodePassword, String encodePhone) {
        return new MemberEntity(
                domain.email(),
                domain.name(),
                encodePassword,
                encodePhone,
                "N",
                Role.ROLE_PENDING
        );
    }

    public static MemberEntity update(MemberEntity member, String encodePassword, String encodePhone) {
        if(encodePassword != null && !encodePassword.isEmpty()) {
            member.password = encodePassword;
        }
        member.phone = encodePhone;
        return member;
    }

    public static MemberEntity passwordUpdate(MemberEntity member, String encodePassword) {
        member.password = encodePassword;
        return member;
    }

    public void setShipping(ShippingEntity shipping) {
        this.shipping.add(shipping);
    }

    public static void approvalMember(MemberEntity member, String approvalYn) {
        member.role = Role.ROLE_USER;
        member.approvalYn = approvalYn;
        member.approvalDate = new Date();
    }

}
