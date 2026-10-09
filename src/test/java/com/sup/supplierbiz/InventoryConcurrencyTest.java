package com.sup.supplierbiz;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.sup.supplierbiz.common.exception.BusinessException;
import com.sup.supplierbiz.domain.dto.StockChangeDTO;
import com.sup.supplierbiz.domain.po.Inventory;
import com.sup.supplierbiz.mapper.InventoryMapper;
import com.sup.supplierbiz.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 库存并发扣减实验：Redisson 分布式锁 + 乐观锁 CAS 重试
 * <p>
 * 实验设计：100 线程同时扣 1 件、初始库存 10、期望成功 10 次、失败 90 次、最终 available=0 不为负。
 * <p>
 * 对照实验（无锁版 / 纯乐观锁版）的实验数据见 docs/dev-log/Day4.md，本测试类仅验证修复后结果。
 *
 * @author 23219
 * @date 2026-10-09
 */
@SpringBootTest
class InventoryConcurrencyTest {

    private static final Long TEST_MATERIAL_ID = 1L;
    private static final Long TEST_WAREHOUSE_ID = 1L;
    private static final BigDecimal INITIAL_STOCK = new BigDecimal("10");
    private static final int THREAD_COUNT = 100;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryMapper inventoryMapper;

    @BeforeEach
    void resetStock() {
        // 直接 UPDATE 绕开乐观锁拦截器（saveOrUpdate 走 updateById 会被乐观锁挡）
        LambdaUpdateWrapper<Inventory> wrapper = new LambdaUpdateWrapper<Inventory>()
                .eq(Inventory::getMaterialId, TEST_MATERIAL_ID)
                .eq(Inventory::getWarehouseId, TEST_WAREHOUSE_ID)
                .set(Inventory::getStockQuantity, INITIAL_STOCK)
                .set(Inventory::getAvailableQuantity, INITIAL_STOCK)
                .set(Inventory::getFrozenQuantity, BigDecimal.ZERO)
                .set(Inventory::getVersion, 0);
        boolean updated = inventoryService.update(wrapper);
        if (!updated) {
            // 无记录则新建一条
            Inventory newInventory = new Inventory();
            newInventory.setMaterialId(TEST_MATERIAL_ID);
            newInventory.setWarehouseId(TEST_WAREHOUSE_ID);
            newInventory.setStockQuantity(INITIAL_STOCK);
            newInventory.setAvailableQuantity(INITIAL_STOCK);
            newInventory.setFrozenQuantity(BigDecimal.ZERO);
            newInventory.setVersion(0);
            inventoryService.save(newInventory);
        }
    }

    /**
     * 修复后并发测试：验证 Redisson 锁 + 乐观锁能挡住超卖。
     * <p>
     * 期望：100 线程并发扣减 → 成功 10 次、失败 90 次、最终 availableQuantity=0。
     */
    @Test
    void testOversellWithLock() throws InterruptedException {
        // 阿里手册第三节【强制】：禁用 Executors，必须用 ThreadPoolExecutor 显式参数
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                THREAD_COUNT, THREAD_COUNT,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(THREAD_COUNT)
        );
        CountDownLatch startGun = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(THREAD_COUNT);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                try {
                    startGun.await();
                    StockChangeDTO dto = new StockChangeDTO(TEST_MATERIAL_ID, TEST_WAREHOUSE_ID, BigDecimal.ONE);
                    inventoryService.stockOut(dto);
                    successCount.incrementAndGet();
                } catch (BusinessException e) {
                    failCount.incrementAndGet();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    failCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    finishLatch.countDown();
                }
            });
        }

        startGun.countDown();
        boolean finished = finishLatch.await(60, TimeUnit.SECONDS);
        assertTrue(finished, "所有线程应在 60s 内完成");
        executor.shutdown();

        // 期望：成功 10 次、失败 90 次
        assertEquals(10, successCount.get(), "成功次数应为 10");
        assertEquals(90, failCount.get(), "失败次数应为 90");

        // 期望：库存正好为 0，不为负（用 compareTo 比较，BigDecimal.equals 受 scale 影响）
        Inventory finalInventory = inventoryService.lambdaQuery()
                .eq(Inventory::getMaterialId, TEST_MATERIAL_ID)
                .eq(Inventory::getWarehouseId, TEST_WAREHOUSE_ID)
                .one();
        assertEquals(0, finalInventory.getAvailableQuantity().compareTo(BigDecimal.ZERO), "可用库存应正好为 0");
        assertTrue(finalInventory.getAvailableQuantity().compareTo(BigDecimal.ZERO) >= 0, "可用库存不能为负");
    }
}
