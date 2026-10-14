package com.sup.supplierbiz.domain.vo;

import com.sup.supplierbiz.common.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 采购订单列表 VO
 *
 * @author Gabe
 * @date 2026-10-14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderVO {
    /** 订单id */
    private Long id;
    /** 订单编号 */
    private String poNo;
    /** 供应商id */
    private Long supplierId;
    /** 供应商名称 */
    private String supplierName;
    /** 下单日期 */
    private LocalDate orderDate;
    /** 期望交付日期 */
    private LocalDate expectedDeliveryDate;
    /** 不含税总额 */
    private BigDecimal totalAmount;
    /** 税额合计 */
    private BigDecimal totalTax;
    /** 价税合计 */
    private BigDecimal totalAmountWithTax;
    /** 订单状态 */
    private OrderStatus status;
    /** 驳回原因 */
    private String rejectReason;
    /** 付款条款 */
    private String paymentTerms;
    /** 备注 */
    private String remarks;
    /** 创建时间 */
    private LocalDateTime createdAt;
    /** 更新时间 */
    private LocalDateTime updatedAt;
}
