package com.sup.supplierbiz.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sup.supplierbiz.common.enums.Deleted;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 采购订单明细持久化实体
 *
 * @author sup
 * @date 2026-10-09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "purchase_order_items")
public class PurchaseOrderItem {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long poId;
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
    @TableLogic
    private Deleted deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
