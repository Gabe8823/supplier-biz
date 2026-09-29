package com.sup.supplierbiz.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sup.supplierbiz.common.enums.Deleted;
import com.sup.supplierbiz.common.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@TableName(value = "materials")
public class Materials {
    @TableId(type = IdType.AUTO)
    private Long id ; //物料id
    private String materialCode;//物料编号
    private String materialName;//物料名称
    private String specification;//物料规格
    private String unit;//物料单位
    private String category;//物料种类
    private BigDecimal safetyStock;//安全库存

    private LocalDateTime createdAt;//创建时间
    private LocalDateTime updatedAt;//更新时间
    private Status status;//状态
    @TableLogic
    private Deleted deleted;//删除 1/0

}
