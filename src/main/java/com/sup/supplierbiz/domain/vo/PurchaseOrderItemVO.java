package com.sup.supplierbiz.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 采购订单明细VO
 *
 * @author 23219
 * @date 2026-10-05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderItemVO {

    private Long materialId;

    private String specification;

    private String unit;

    private BigDecimal orderQuantity;

    private BigDecimal receivedQuantity;

    private BigDecimal unitPrice;

    private BigDecimal taxRate;

    private BigDecimal taxAmount;

    private BigDecimal amount;

    private BigDecimal amountWithTax;

    private Integer sortOrder;
}
