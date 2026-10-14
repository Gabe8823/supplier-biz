package com.sup.supplierbiz.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sup.supplierbiz.common.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 供应商展示 VO
 *
 * @author Gabe
 * @date 2026-10-12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuppliersVO {
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
}
