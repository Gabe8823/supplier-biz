package com.sup.supplierbiz.domain.vo;

import com.sup.supplierbiz.common.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 采购订单详情VO（主表信息+明细列表）
 *
 * @author 23219
 * @date 2026-10-05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderDetailVO {

    private Long id;

    private String poNo;

    private Long supplierId;

    private LocalDate orderDate;

    private LocalDate expectedDeliveryDate;

    private BigDecimal totalAmount;

    private BigDecimal totalTax;

    private BigDecimal totalAmountWithTax;

    private OrderStatus status;

    private String paymentTerms;

    private String remarks;

    private LocalDateTime createdAt;

    private LocalDateTime reviewedAt;

    private List<PurchaseOrderItemVO> items;
}
