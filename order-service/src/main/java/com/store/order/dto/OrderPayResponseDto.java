package com.store.order.dto;

import com.store.order.entity.OrderEntity;
import com.store.order.entity.OrderPayDetailEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class OrderPayResponseDto {
   private Long id;
   private Long memberId;
   private String resultCd;
   private String resultMsg;
   private String payMethod;
   private String nointFlg;
   private String tid;
   private String appDtm;
   private String appNo;
   private String ordNo;
   private String goodsName;
   private String amt;
   private String ordNm;
   private String fnNm;
   private String cancelYN;
   private String mbsReserved;
   private String appCardCd;
   private String acqCardCd;
   private String cardNo;
   private String quota;
   private String usePointAmt;
   private String cardType;
   private String authType;
   private String cashCrctFlg;
   private String bankCd;
   private String vacntNo;
   private String lmtDay;
   private String socHpNo;
   private String crctNo;
   private String crctType;
   private String cancelDtm;

   public static OrderPayResponseDto from(OrderPayDetailEntity entity) {
      return OrderPayResponseDto.builder()
              .resultCd(entity.getResultCd())
              .amt(entity.getAmt())
              .tid(entity.getTid())
              .appDtm(entity.getAppDtm())
              .appNo(entity.getAppNo())
              .fnNm(entity.getFnNm())
              .cardNo(entity.getCardNo())
              .quota(entity.getQuota())
              .nointFlg(entity.getNointFlg())
              .usePointAmt(entity.getUsePointAmt())
              .cardType(entity.getCardType())
              .authType(entity.getAuthType())
              .cashCrctFlg(entity.getCashCrctFlg())
              .vacntNo(entity.getVacntNo())
              .lmtDay(entity.getLmtDay())
              .socHpNo(entity.getSocHpNo())
              .cancelDtm(entity.getCancelDtm())
              .payMethod(entity.getOrders().getPayType().name())
              .build();
   }
}
