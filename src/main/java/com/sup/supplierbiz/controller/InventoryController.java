package com.sup.supplierbiz.controller;

import com.sup.supplierbiz.common.result.Result;
import com.sup.supplierbiz.domain.dto.PageDTO;
import com.sup.supplierbiz.domain.dto.PageQuery;
import com.sup.supplierbiz.domain.dto.StockChangeDTO;
import com.sup.supplierbiz.domain.vo.InventoryVO;
import com.sup.supplierbiz.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
@Tag(name="库存管理模块")
@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @Operation(summary = "分页查询库存")
    @GetMapping("/page")
    public Result<PageDTO<InventoryVO>> pageInventory(
            @ParameterObject PageQuery query,
            @RequestParam(required = false) String materialName
            ){

        return Result.success(inventoryService.pageInventory(query,materialName));
    }
    @Operation(summary = "入库")
    @PostMapping("/stock-in")
    public Result<Long> stockIn(@RequestBody StockChangeDTO dto) {
        return Result.success(inventoryService.stockIn(dto));
    }
    @Operation(summary = "出库")
    @PostMapping("/stock-out")
    public Result<Void> stockOut(@RequestBody StockChangeDTO dto) {
        inventoryService.stockOut(dto);
        return Result.success();
    }
}
