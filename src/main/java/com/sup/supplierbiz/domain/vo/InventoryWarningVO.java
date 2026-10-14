package com.sup.supplierbiz.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 *
 * @author Gabe
 * @date 2026-10-14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryWarningVO {
    /**
     * 主键ID
     */

    private Long id;

    /**
     * 物料ID
     */
    private Long materialId;
    private String materialName;
    /**
     * 仓库ID
     */
    private Long warehouseId;
    private String warehouseName;
    /**
     * 当前库存量
     */
    private BigDecimal stockQuantity;

    /**
     * 可用库存量
     */
    private BigDecimal availableQuantity;

    /**
     * 冻结/预占库存量
     */
    private BigDecimal frozenQuantity;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

}
