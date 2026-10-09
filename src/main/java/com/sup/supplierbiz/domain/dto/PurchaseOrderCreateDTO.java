package com.sup.supplierbiz.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
/**
 * 创建采购订单 DTO
 *
 * @author sup
 * @date 2026-10-09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderCreateDTO {
    /** 供应商id */ private Long supplierId;
    private LocalDate orderDate;
    private LocalDate expectedDeliveryDate;
    private String paymentTerms;
    private String remarks;
    private List<Items> items;
}
