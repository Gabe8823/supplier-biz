package com.sup.supplierbiz.domain.po;

import com.baomidou.mybatisplus.annotation.*;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 库存表
 * @TableName inventory
 */
@TableName(value ="inventory")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 物料ID
     */
    private Long materialId;

    /**
     * 仓库ID
     */
    private Long warehouseId;

    /**
     * 当前库存量
     */
    private BigDecimal stockQuantity;

    /**
     * 可用库存量
     */
    private BigDecimal availableQuantity;

    /**
     * 冻结/预占库存量
     */
    private BigDecimal frozenQuantity;

    /**
     * 乐观锁版本号
     * <p>D4-4 超卖复现阶段刻意不使用 @Version，且不注册 OptimisticLockerInnerInterceptor，
     * 保留 stockOut "先查后改"的非原子实现以复现丢失更新；
     * 后续乐观锁修复阶段再将注解与拦截器一同加回。</p>
     */
    @Version
    private Integer version;

    /**
     * 逻辑删除：0-未删，1-已删
     */
    @TableLogic
    private Integer deleted;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;


}