package com.store.order.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderInvoiceRequestDto {
   private String ordNo;
   private String invoiceNo;
   private String invoiceName;
}
