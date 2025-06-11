package com.store.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MemberOrderPayResponseDto {
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
}
