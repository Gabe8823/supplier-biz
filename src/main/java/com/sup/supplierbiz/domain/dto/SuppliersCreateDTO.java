package com.sup.supplierbiz.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 供应商新增 DTO
 *
 * @author Gabe
 * @date 2026-10-12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuppliersCreateDTO {
    /**
     * 供应商编码
     */
    @NotBlank(message = "供应商编号不能为空")
    private String supplierCode;

    /**
     * 供应商名称
     */
    @NotBlank(message = "供应商名称不能为空")
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


}
