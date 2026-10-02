package com.sup.supplierbiz.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Items {
    private Long materialId;
    private BigDecimal orderQuantity;
    private BigDecimal unitPrice;
    private BigDecimal taxRate;
}
