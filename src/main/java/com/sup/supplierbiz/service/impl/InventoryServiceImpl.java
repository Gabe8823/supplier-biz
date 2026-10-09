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
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

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
    private final RedissonClient redissonClient;

    /**
     * 库存分布式锁 key 前缀，粒度细到 (materialId, warehouseId)，避免锁全表互不相干的库存行
     */
    private static final String STOCK_LOCK_KEY_PREFIX = "lock:inventory:";

    @Override
    public PageDTO<InventoryVO> pageInventory(PageQuery query, String materialName) {
        Page<InventoryVO> page = Page.of(query.getPageNum(), query.getPageSize());
        // MP 分页插件会自动给 SQL 拼 LIMIT，并把 total/records 回填到传入的 page 对象
        inventoryMapper.selectPageVO(page, materialName);
        return PageDTO.of(page);
    }

    /**
     * 入库：有记录累加 / 无记录新建；Redisson 锁 + 乐观锁 CAS 重试。
     * <p>
     * 不加 @Transactional：锁内自旋重试与事务边界冲突，事务会让 update 的可见性推迟，
     * 自旋读到的还是旧值，最终死循环。锁内读-改-写本身在单线程临界区已原子。
     */
    @Override
    public Long stockIn(StockChangeDTO dto) {
        if (dto.getQuantity() == null || dto.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ResultCode.PARAM_ERROR);
        }
        String lockKey = STOCK_LOCK_KEY_PREFIX + dto.getMaterialId() + ":" + dto.getWarehouseId();
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        try {
            while (true) {
                Inventory inventory = lambdaQuery().eq(Inventory::getMaterialId, dto.getMaterialId())
                        .eq(Inventory::getWarehouseId, dto.getWarehouseId())
                        .one();
                if (inventory != null) {
                    inventory.setStockQuantity(inventory.getStockQuantity().add(dto.getQuantity()));
                    inventory.setAvailableQuantity(inventory.getAvailableQuantity().add(dto.getQuantity()));
                    // 乐观锁：updateById 自动带 where version=? + version+1，冲突返回 false
                    if (updateById(inventory)) {
                        return inventory.getId();
                    }
                    // CAS 失败：版本冲突，自旋重试
                    continue;
                }
                Inventory newInventory = new Inventory();
                newInventory.setMaterialId(dto.getMaterialId());
                newInventory.setWarehouseId(dto.getWarehouseId());
                newInventory.setStockQuantity(dto.getQuantity());
                newInventory.setAvailableQuantity(dto.getQuantity());
                newInventory.setFrozenQuantity(BigDecimal.ZERO);
                save(newInventory);
                return newInventory.getId();
            }
        } finally {
            // unlock 必须 finally：异常时也释放，避免锁泄漏
            lock.unlock();
        }
    }

    /**
     * 出库扣减：Redisson 分布式锁 + 乐观锁 CAS 重试。
     * <p>
     * 主方案：分布式锁——库存是热点行，扣减冲突高，排队等待优于自旋重试。
     * 兜底：锁内仍带乐观锁——若看门狗超时或锁异常释放，乐观锁仍能挡住丢失更新。
     */
    @Override
    public void stockOut(StockChangeDTO dto) {
        if (dto.getQuantity() == null || dto.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ResultCode.PARAM_ERROR);
        }
        String lockKey = STOCK_LOCK_KEY_PREFIX + dto.getMaterialId() + ":" + dto.getWarehouseId();
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        try {
            while (true) {
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
                if (updateById(inventory)) {
                    return;
                }
                // CAS 失败：版本冲突，自旋重试
            }
        } finally {
            lock.unlock();
        }
    }
}
