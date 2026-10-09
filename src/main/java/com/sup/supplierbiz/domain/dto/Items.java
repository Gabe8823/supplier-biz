package com.sup.supplierbiz.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 订单明细项 DTO
 *
 * @author sup
 * @date 2026-10-09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Items {
    private Long materialId;
    private BigDecimal orderQuantity;
    private BigDecimal unitPrice;
    private BigDecimal taxRate;
}
