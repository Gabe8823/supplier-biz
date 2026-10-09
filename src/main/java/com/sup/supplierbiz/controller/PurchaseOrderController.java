package com.sup.supplierbiz.controller;

import com.sup.supplierbiz.common.result.Result;
import com.sup.supplierbiz.domain.dto.PurchaseOrderCreateDTO;
import com.sup.supplierbiz.domain.vo.PurchaseOrderDetailVO;
import com.sup.supplierbiz.service.PurchaseOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 采购订单管理接口
 *
 * @author sup
 * @date 2026-10-09
 */
@RestController
@RequestMapping("/api/purchase-orders")
@Tag(name = "采购订单模块")
@RequiredArgsConstructor
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;

    @Operation(summary = "创建订单")
    @PostMapping()
    public Result<Long> create(@RequestBody PurchaseOrderCreateDTO dto,
                               HttpServletRequest request) {
        Long userId = Long.valueOf(request.getAttribute("userId").toString());

        return Result.success(purchaseOrderService.createOrder(dto,userId));
    }
    @Operation(summary = "查询订单详情")
    @GetMapping("/{id}")
    public Result<PurchaseOrderDetailVO> findById(@PathVariable Long id){
        return Result.success(purchaseOrderService.getDetail(id));
    }
}