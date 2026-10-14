package com.sup.supplierbiz.controller;

import com.sup.supplierbiz.common.enums.OrderStatus;
import com.sup.supplierbiz.common.result.Result;
import com.sup.supplierbiz.domain.dto.PageDTO;
import com.sup.supplierbiz.domain.dto.PageQuery;
import com.sup.supplierbiz.domain.dto.PurchaseOrderCreateDTO;
import com.sup.supplierbiz.domain.vo.PurchaseOrderDetailVO;
import com.sup.supplierbiz.domain.vo.PurchaseOrderVO;
import com.sup.supplierbiz.service.PurchaseOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * 采购订单管理接口
 *
 * @author Gabe
 * @date 2026-10-09
 */
@RestController
@RequestMapping("/api/purchase-orders")
@Tag(name = "采购订单模块")
@RequiredArgsConstructor
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;

    @Operation(summary = "创建订单")
    @PostMapping
    public Result<Long> create(@RequestBody PurchaseOrderCreateDTO dto,
                               HttpServletRequest request) {
        Long userId = Long.valueOf(request.getAttribute("userId").toString());

        return Result.success(purchaseOrderService.createOrder(dto, userId));
    }

    @Operation(summary = "查询订单详情")
    @GetMapping("/{id}")
    public Result<PurchaseOrderDetailVO> findById(@PathVariable Long id) {
        return Result.success(purchaseOrderService.getDetail(id));
    }

    @Operation(summary = "审核订单")
    @PutMapping("/{id}/approve")
    public Result<Void> approveById(@PathVariable Long id, HttpServletRequest request) {
        Long userId = Long.valueOf(request.getAttribute("userId").toString());
        purchaseOrderService.approveById(id, userId);
        return Result.success();
    }

    @Operation(summary = "驳回订单")
    @PutMapping("/{id}/reject")
    public Result<Void> rejectById(@PathVariable Long id, @RequestParam String reason, HttpServletRequest request) {
        Long userId = Long.valueOf(request.getAttribute("userId").toString());
        purchaseOrderService.rejectById(id, reason, userId);
        return Result.success();
    }

    @Operation(summary = "订单分页查询")
    @GetMapping("/page")
    public Result<PageDTO<PurchaseOrderVO>> pageOrder(@ParameterObject PageQuery query,
                                                      @RequestParam(required = false) String supplierName,
                                                      @RequestParam(required = false) OrderStatus status,
                                                      @RequestParam(required = false) LocalDate startDate,
                                                      @RequestParam(required = false) LocalDate endDate) {
        return Result.success(purchaseOrderService.pageOrder(query, supplierName, status, startDate, endDate));
    }
    @Operation(summary = "订单作废")
    @PutMapping("/{id}/cancel")
    public Result<Void> cancelById(@PathVariable Long id,  HttpServletRequest request) {
        Long userId = Long.valueOf(request.getAttribute("userId").toString());
        purchaseOrderService.cancelById(id, userId);
        return Result.success();
    }
}