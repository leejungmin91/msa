package com.store.order.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.common.util.DataEncrypt;
import lombok.Builder;

import java.text.SimpleDateFormat;
import java.util.*;

@Builder
public record OrderResultDomain(
        String resultCd, String resultMsg, String payMethod, String nointFlg, String tid
        , String appDtm, String appNo, String ordNo, String goodsName, String amt
        , String ordNm, String fnNm, String cancelYN, String mbsReserved, String appCardCd
        , String acqCardCd, String cardNo, String quota, String usePointAmt, String cardType
        , String authType, String cashCrctFlg, String bankCd, String vacntNo, String lmtDay
        , String socHpNo, String crctNo, String crctType) {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static OrderResultDomain fromMap(Map<String, String> resultMap) {
        return MAPPER.convertValue(resultMap, OrderResultDomain.class);
    }
}
