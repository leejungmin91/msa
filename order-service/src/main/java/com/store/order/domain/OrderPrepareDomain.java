package com.store.api.order.domain;

import com.store.common.util.DataEncrypt;
import lombok.Builder;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Builder
public record OrderPrepareDomain(
        Long memberId
        , List<OrderItemDomain> orderItems
        , String txId
        , String payMethod
        , String trxCd
        , String mid
        , String goodsNm
        , String ordNo
        , Long goodsAmt
        , String ordNm
        , String ordTel
        , String orderAddress
        , String orderAddressDetail
        , String orderZipcode
        , String returnUrl
        , String merchantKey
        , String currencyType
        , String encData
        , String ediDate
        , String model) {

    private static final DateTimeFormatter EDI_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public OrderPrepareDomain prepare(Long memberId, String mid, String returnUrl, String merchantKey, String currencyType, String model) {
        String transactionId = "TX" + UUID.randomUUID();

        Long totalAmt = orderItems.stream()
                .mapToLong(i -> i.getOrderPrice().intValue())
                .sum();
        String goods = orderItems.size() == 1
                ? orderItems.get(0).getProductName()
                : orderItems.get(0).getProductName() + " 외 " + (orderItems.size() - 1) + "건";


        String ediDate = LocalDateTime.now().format(EDI_FORMATTER);                                             // 전문 생성일시
        String encData = DataEncrypt.encrypt(mid + ediDate + totalAmt + merchantKey);    // Hash 값

        // 1000~9999 사이 랜덤 4자리
        String ordNo = ediDate + ThreadLocalRandom.current().nextInt(1000, 10000);

        return new OrderPrepareDomain(memberId, this.orderItems, transactionId
                , this.payMethod, "0", mid, goods, ordNo, totalAmt
                , this.ordNm, this.ordTel, this.orderAddress, this.orderAddressDetail, this.orderZipcode, returnUrl, merchantKey
                , currencyType, encData, ediDate, model);
    }
}
