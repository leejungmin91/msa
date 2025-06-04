package com.store.api.member.entity;

import com.store.api.member.domain.MemberUpdateDomain;
import com.store.api.member.domain.ShippingCreateDomain;
import com.store.api.member.domain.ShippingUpdateDomain;
import com.store.common.config.security.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Getter
@Table(name = "SHIPPING")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@Entity
public class ShippingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    private String subject;

    private String name;

    private String phone;

    private String address;

    private String addressDetail;

    private String zipcode;

    @Column(columnDefinition = "CHAR(1) DEFAULT 'N'")
    private String mainYn;

    @CreatedDate
    private Date created;

    @LastModifiedDate
    private Date modified;

    // 생성 메서드
    public static ShippingEntity create(ShippingCreateDomain domain) {
        ShippingEntity entity = new ShippingEntity();

        entity.subject = domain.subject();
        entity.name = domain.name();
        entity.phone = domain.phone();
        entity.address = domain.address();
        entity.addressDetail = domain.addressDetail();
        entity.zipcode = domain.zipcode();
        entity.mainYn = domain.mainYn();
        return entity;
    }

    public ShippingEntity updateShipping(ShippingUpdateDomain domain) {
        this.subject = domain.subject();
        this.name = domain.name();
        this.phone = domain.phone();
        this.address = domain.address();
        this.addressDetail = domain.addressDetail();
        this.zipcode = domain.zipcode();
        this.mainYn = domain.mainYn();

        return this;
    }

    public void changeMainYn(String mainYn) {
        this.mainYn = mainYn;
    }

    public void addMember(MemberEntity member) {
        this.member = member;
    }

}
