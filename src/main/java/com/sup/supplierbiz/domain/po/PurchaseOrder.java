package com.sup.supplierbiz.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sup.supplierbiz.common.enums.Deleted;
import com.sup.supplierbiz.common.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "purchase_orders")
public class PurchaseOrder {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;//订单id
    private String poNo;//订单编号
    private Long supplierId;//供应商id
    private LocalDate orderDate;
    private LocalDate expectedDeliveryDate;
    private BigDecimal totalAmount;
    private BigDecimal totalTax;
    private BigDecimal totalAmountWithTax;
    private OrderStatus status;
    @TableLogic
    private Deleted deleted;
    private String paymentTerms;
    private String remarks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;
    private LocalDateTime reviewedAt;
    private Long reviewedBy;
}
