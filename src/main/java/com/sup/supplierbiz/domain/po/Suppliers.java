package com.sup.supplierbiz.domain.po;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

import com.sup.supplierbiz.common.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 供应商表
 * @TableName suppliers
 *
 * @author sup
 * @date 2026-10-09
 */
@TableName(value ="suppliers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Suppliers {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 供应商编码
     */
    private String supplierCode;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 简称
     */
    private String shortName;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 地址
     */
    private String address;

    /**
     * 税号
     */
    private String taxId;

    /**
     * 银行信息
     */
    private String bankInfo;

    /**
     * 供应商类型：1-原材料，2-设备，3-服务
     */
    private Integer supplierType;

    /**
     * 状态：0-禁用，1-启用
     */
    private Status status;

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