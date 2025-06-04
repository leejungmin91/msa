package com.store.api.order.dto;

import com.store.api.order.domain.OrderItemDomain;
import com.store.api.order.entity.OrderPayDetailEntity;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderCancelResponseDto {
    private String resultCd;
    private String resultMsg;
    private String payMethod;
    private String tid;
    private String appDtm;
    private String appNo;
    private String ordNo;
    private String amt;
    private String cancelYN;
    private String mbsReserved;

}

