package com.sup.supplierbiz.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderCreateDTO {
    private Long supplierId;//供应商id
    private LocalDate orderDate;
    private LocalDate expectedDeliveryDate;
    private String paymentTerms;
    private String remarks;
    private List<Items> items;
}
