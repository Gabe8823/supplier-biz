package com.sup.supplierbiz.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sup.supplierbiz.common.enums.ResultCode;
import com.sup.supplierbiz.common.exception.BusinessException;
import com.sup.supplierbiz.domain.dto.PageDTO;
import com.sup.supplierbiz.domain.dto.PageQuery;
import com.sup.supplierbiz.domain.dto.StockChangeDTO;
import com.sup.supplierbiz.domain.po.Inventory;
import com.sup.supplierbiz.domain.vo.InventoryVO;
import com.sup.supplierbiz.service.InventoryService;
import com.sup.supplierbiz.mapper.InventoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @author 23219
 * @description 针对表【inventory(库存表)】的数据库操作Service实现
 * @createDate 2026-10-01 15:03:02
 */
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory>
        implements InventoryService {
    private final InventoryMapper inventoryMapper;

    @Override
    public PageDTO<InventoryVO> pageInventory(PageQuery query, String materialName) {
        Page<InventoryVO> page = Page.of(query.getPageNum(), query.getPageSize());
        // MP 分页插件会自动给 SQL 拼 LIMIT，并把 total/records 回填到传入的 page 对象
        inventoryMapper.selectPageVO(page, materialName);
        return PageDTO.of(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long stockIn(StockChangeDTO dto) {

        if (dto.getQuantity() == null || dto.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ResultCode.PARAM_ERROR);
        }
        Inventory inventory = lambdaQuery().eq(Inventory::getMaterialId, dto.getMaterialId())
                .eq(Inventory::getWarehouseId, dto.getWarehouseId())
                .one();

        if (inventory != null) {
            inventory.setStockQuantity(inventory.getStockQuantity().add(dto.getQuantity()));
            inventory.setAvailableQuantity(inventory.getAvailableQuantity().add(dto.getQuantity()));
            updateById(inventory);
            return inventory.getId();
        } else {
            Inventory newInventory = new Inventory();
            newInventory.setMaterialId(dto.getMaterialId());
            newInventory.setWarehouseId(dto.getWarehouseId());
            newInventory.setStockQuantity(dto.getQuantity());
            newInventory.setAvailableQuantity(dto.getQuantity());
            newInventory.setFrozenQuantity(BigDecimal.ZERO);
            save(newInventory);
            return newInventory.getId();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void stockOut(StockChangeDTO dto) {
        if (dto.getQuantity() == null || dto.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ResultCode.PARAM_ERROR);
        }
        Inventory inventory = lambdaQuery().eq(Inventory::getMaterialId, dto.getMaterialId())
                .eq(Inventory::getWarehouseId, dto.getWarehouseId())
                .one();
        if (inventory == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        if (inventory.getAvailableQuantity().compareTo(dto.getQuantity()) < 0) {
            throw new BusinessException(ResultCode.STOCK_NOT_ENOUGH);
        }
            inventory.setStockQuantity(inventory.getStockQuantity().subtract(dto.getQuantity()));
            inventory.setAvailableQuantity(inventory.getAvailableQuantity().subtract(dto.getQuantity()));
            updateById(inventory);
    }
}




