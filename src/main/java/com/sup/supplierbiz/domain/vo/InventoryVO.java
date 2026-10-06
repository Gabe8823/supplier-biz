package com.sup.supplierbiz.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 库存展示VO（含物料名/仓库名）
 *
 * @author 23219
 * @date 2026-10-06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryVO {

    private Long id;

    private Long materialId;

    private Long warehouseId;

    private String materialName;

    private String warehouseName;

    private BigDecimal stockQuantity;

    private BigDecimal availableQuantity;

    private BigDecimal frozenQuantity;
}
