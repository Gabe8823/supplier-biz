package com.sup.supplierbiz.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sup.supplierbiz.common.enums.Deleted;
import com.sup.supplierbiz.common.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 物料持久化实体
 *
 * @author sup
 * @date 2026-10-09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "materials")
public class Materials {
    @TableId(type = IdType.AUTO)
    /** 物料id */ private Long id;
    /** 物料编号 */ private String materialCode;
    /** 物料名称 */ private String materialName;
    /** 物料规格 */ private String specification;
    /** 物料单位 */ private String unit;
    /** 物料种类 */ private String category;
    /** 安全库存 */ private BigDecimal safetyStock;

    /** 创建时间 */ private LocalDateTime createdAt;
    /** 更新时间 */ private LocalDateTime updatedAt;
    /** 状态 */ private Status status;
    @TableLogic
    /** 删除 1/0 */ private Deleted deleted;

}
