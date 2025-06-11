package com.store.order.entity;

import com.store.member.dto.MemberResponseDto;
import com.store.member.entity.MemberEntity;
import com.store.order.domain.OrderResultDomain;
import com.store.order.dto.OrderCancelResponseDto;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "ORDER_PAY_DETAIL")
public class OrderPayDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity orders;

    private String resultCd; // 결과코드

    private String amt; // 거래금액

    private String tid; // 거래번호

    private String appDtm; // 결제일시

    private String appNo; // 승인번호

    private String fnNm; // 카드사명

    private String cancelYn; // 결제취소여보

    private String appCardCd; // 발급사코드

    private String acqCardCd; // 매입사코드

    private String quota; // 카드 할부기간

    private String nointFlg; // 분담무이자구분

    private String usePointAmt; // 사용포인트양

    private String cardType; // 카드타입(0:신용,1:체크)

    private String authType; // 인증타입(01:keyin,02:ISP,03:VISA)

    private String cashCrctFlg; // 현금영수증(0:사용안함,1:사용)

    private String vacntNo; // 가상계좌 번호

    private String lmtDay; // 입금기한

    private String socHpNo; // 휴대폰번호

    private String cardNo; // 마스킹카드번호

    private String cancelDtm; // 취소일시

    @Enumerated(EnumType.STRING)
    private PayStatus payStatus;

    private LocalDateTime paidAt;

    // 결제 완료 후 생성
    public static OrderPayDetailEntity create(OrderResultDomain orderResultDomain, OrderEntity orders) {
        OrderPayDetailEntity orderPayDetail = new OrderPayDetailEntity();
        orderPayDetail.resultCd = orderResultDomain.resultCd();
        orderPayDetail.amt = orderResultDomain.amt();
        orderPayDetail.tid = orderResultDomain.tid();
        orderPayDetail.appDtm = orderResultDomain.appDtm();
        orderPayDetail.appNo = orderResultDomain.appNo();
        orderPayDetail.fnNm = orderResultDomain.fnNm();
        orderPayDetail.cancelYn = orderResultDomain.cancelYN();
        orderPayDetail.appCardCd = orderResultDomain.appCardCd();
        orderPayDetail.acqCardCd = orderResultDomain.acqCardCd();
        orderPayDetail.quota = orderResultDomain.quota();
        orderPayDetail.nointFlg = orderResultDomain.nointFlg();
        orderPayDetail.usePointAmt = orderResultDomain.usePointAmt();
        orderPayDetail.cardType = orderResultDomain.cardType();
        orderPayDetail.authType = orderResultDomain.authType();
        orderPayDetail.cashCrctFlg = orderResultDomain.cashCrctFlg();
        orderPayDetail.vacntNo = orderResultDomain.vacntNo();
        orderPayDetail.lmtDay = orderResultDomain.lmtDay();
        orderPayDetail.socHpNo = orderResultDomain.socHpNo();
        orderPayDetail.cardNo = orderResultDomain.cardNo();
        orderPayDetail.orders = orders;
        return orderPayDetail;
    }

    public static OrderPayDetailEntity from(OrderEntity order){
        OrderPayDetailEntity orderPayDetail = order.getOrderPayDetail();
        return orderPayDetail;
    }

    public void cancel(OrderEntity order, OrderCancelResponseDto dto) {
        order.getOrderPayDetail().resultCd = dto.getResultCd();
        this.cancelYn = "Y";
        this.cancelDtm = dto.getAppDtm();
        this.appNo = order.getOrderPayDetail().appNo;
        this.payStatus = PayStatus.CANCELED;
    }
}
