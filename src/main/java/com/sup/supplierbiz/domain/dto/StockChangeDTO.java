package com.sup.supplierbiz.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockChangeDTO {
    private Long materialId;
    private Long warehouseId;
    private BigDecimal quantity;
}
