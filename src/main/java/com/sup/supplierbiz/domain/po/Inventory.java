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
     */
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