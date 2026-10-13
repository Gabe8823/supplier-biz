package com.sup.supplierbiz.controller;

import com.sup.supplierbiz.common.enums.Status;
import com.sup.supplierbiz.common.result.Result;
import com.sup.supplierbiz.domain.dto.PageDTO;
import com.sup.supplierbiz.domain.dto.PageQuery;
import com.sup.supplierbiz.domain.dto.SuppliersCreateDTO;
import com.sup.supplierbiz.domain.dto.SuppliersUpdateDTO;
import com.sup.supplierbiz.domain.vo.SuppliersVO;
import com.sup.supplierbiz.service.SuppliersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name="供应商管理模块")
@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SuppliersController {
    private final SuppliersService suppliersService;

    @Operation(summary = "供应商分页列表")
    @GetMapping("/page")
    public Result<PageDTO<SuppliersVO>> pageSuppliers(
            @ParameterObject PageQuery query,@Parameter(description = "供应商名称")
            @RequestParam(required = false) String supplierName){

        return Result.success(suppliersService.pageSuppliers(query,supplierName));
    }
    @Operation(summary = "根据id查找供应商")
    @GetMapping("/{id}")
    public Result<SuppliersVO> findById(@PathVariable Long id){
        return Result.success(suppliersService.findById(id));
    }

    @Operation(summary = "新增供应商")
    @PostMapping
    public Result<Long> addSupplier(@RequestBody @Validated SuppliersCreateDTO createDTO){
        return Result.success(suppliersService.addSupplier(createDTO));
    }

    @Operation(summary = "修改供应商信息")
    @PutMapping
    public Result<Void> updateSupplier(@RequestBody SuppliersUpdateDTO updateDTO){
        return Result.success(suppliersService.updateSupplier(updateDTO));
    }

    @Operation(summary = "根据id删除供应商")
    @DeleteMapping("/{id}")
    public Result<Void> deleteById(@PathVariable Long id){
        return Result.success(suppliersService.deleteById(id));
    }
    @Operation(summary = "修改供应商状态")
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id,@Parameter(description = "状态") @RequestParam Status status){
        return Result.success(suppliersService.updateStatus(id,status));
    }
}
