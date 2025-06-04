package com.store.api.order.dto;

import com.store.api.order.domain.OrderItemDomain;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCancelRequestDto {
   private String tid;          // 거래 ID (필수)
   private String ordNo;        // 주문 번호 (선택)
   private String mid;          // 가맹점 ID (고정값: 예: kistest00m)
   private String canAmt;       // 취소 금액
   private String canId;        // 취소자 ID
   private String canNm;        // 취소자 이름
   private String partCanFlg;   // 0: 전체취소 / 1: 부분취소
   private String canMsg;       // 취소 사유
   private String payMethod;    // 결제수단
}
