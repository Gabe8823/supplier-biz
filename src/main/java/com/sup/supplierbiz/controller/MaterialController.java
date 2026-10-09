package com.sup.supplierbiz.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sup.supplierbiz.common.result.Result;
import com.sup.supplierbiz.domain.dto.PageDTO;
import com.sup.supplierbiz.domain.dto.PageQuery;
import com.sup.supplierbiz.domain.po.Materials;
import com.sup.supplierbiz.service.MaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

/**
 * 物料管理接口
 *
 * @author sup
 * @date 2026-10-09
 */
@Tag(name = "物料管理模块")
@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
public class MaterialController {
    private final MaterialService materialService;
    @Operation(summary = "分页查询物料列表")
    @GetMapping
    public Result<PageDTO<Materials>> page(
            @ParameterObject PageQuery query,
            @Parameter(description = "物料名称，模糊") @RequestParam(required = false) String materialName,
            @Parameter(description = "物料编码，模糊") @RequestParam(required = false) String materialCode){
        LambdaQueryWrapper<Materials> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(materialName),Materials::getMaterialName,materialName)
                .or()
                .like(StrUtil.isNotBlank(materialCode),Materials::getMaterialCode,materialCode)
                .orderByDesc(Materials::getId);
        Page<Materials> page = materialService.page(
                Page.of(query.getPageNum(), query.getPageSize()), queryWrapper);
        return Result.success(PageDTO.of(page));
    }
}
